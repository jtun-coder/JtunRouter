package com.jj.routingservice

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.format.Formatter
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.dolphin.localsocket.Const
import com.dolphin.localsocket.bean.PackageInfo
import com.dolphin.localsocket.cmd.BaseLocalCmd
import com.dolphin.localsocket.cmd.LocalCmd
import com.dolphin.localsocket.listener.LocalServerListener
import com.dolphin.localsocket.server.LocalClientTransceiver
import com.dolphin.localsocket.server.LocalServer
import com.dolphin.localsocket.utils.GsonUtil
import com.jj.routingservice.control.WifiApControl
import com.jj.routingservice.http.HttpServer
import com.jj.routingservice.http.HttpWebServer
import com.jj.routingservice.room.AppDatabase
import com.jj.routingservice.room.AppInfo.Companion.toCompat
import com.jj.routingservice.socket.SocketIO
import com.jj.routingservice.util.DeviceUtil
import com.jj.routingservice.util.FileHelper
import com.jj.routingservice.util.FrpUtil
import com.jj.routingservice.util.JLog
import com.jj.routingservice.util.KLog
import com.jj.routingservice.util.NetworkUtils
import com.jj.routingservice.util.SmsWriteOpUtil
import com.jj.routingservice.util.SystemCtrlUtil
import com.jj.routingservice.util.TetheringUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Runnable
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions


class MainActivity : AppCompatActivity(), EasyPermissions.PermissionCallbacks, LocalServerListener {
    private var httpWeb: HttpWebServer? = null
    private var httpServer: HttpServer? = null
    private val handler = Handler(Looper.getMainLooper())
    private var textSpeed: TextView? = null

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        textSpeed = findViewById(R.id.text_speed)
        getPermission()
    }

    private fun initData() {
        //启动web服务
        httpWeb = HttpWebServer(this@MainActivity)
        httpWeb?.startServer()
        httpServer = HttpServer()
        httpServer?.startServer()
        //启动一个LocalServer用于管理第三方应用
        LocalServer.instance.localServerListener = this
        LocalServer.instance.init(Const.ROUTING_TAG)

        WifiApControl.getInstance().trafficStats()
        WifiApControl.getInstance().initNetSpeed()
        WifiApControl.getInstance().sysRealtime()
        FileHelper.init(this@MainActivity)
//        NetworkUtils.getRSRP(this@MainActivity)
//        startService(Intent(this, TetheringService::class.java)
//            .putExtra(TetheringService.EXTRA_REMOVE_INTERFACE, "swlan0"))
        startAp()
        SystemCtrlUtil.systemSettings(this)
//        SystemCtrlUtil.requestUsageStatsPermission(this) //重启后立即打开应用会呼出设置界面
        SocketIO.getInstance().connect()
//        SmsManager.defaultSmsPackage(this)
    }

    private fun startAp() {
        lifecycleScope.launch {
            WifiApControl.getInstance().setAutoShutdown(false)
            WifiApControl.getInstance().startTethering {
                showInfo()
            }
        }
    }


    private fun showInfo() {
        lifecycleScope.launch {
            SystemCtrlUtil.setenforce()
            delay(2000)
            val textInfo = findViewById<TextView>(R.id.text_info)
            val config = WifiApControl.getInstance().getSoftApConfig()
            val ip = NetworkUtils.getLocalIpv4Address()
            withContext(Dispatchers.Main) {
                val sb = StringBuilder()
                sb.append("WifiName : ${config?.ssid}")
                sb.append("\n")
                sb.append("Default password : ${config?.passphrase}")
                sb.append("\n")
                sb.append("Admin Html : http://$ip:2000")
                sb.append("\n id:" + DeviceUtil.getAndroidId(this@MainActivity))
                textInfo.text = sb.toString()
                if (!SmsWriteOpUtil.isWriteEnabled(applicationContext)) {
                    KLog.i("isWriteEnabled")
                    SmsWriteOpUtil.setWriteEnabled(applicationContext, true);
                }
                JLog.r("init", "start $sb")
                TetheringUtil.startTetheringService()
                FrpUtil.startFrp()
            }
            KLog.i(DeviceUtil.getAndroidId(this@MainActivity))
            handler.postDelayed(getSpeedRunnable, 1000)
        }
    }

    @AfterPermissionGranted(127)
    private fun getPermission() {
        val permis = arrayOf(
            Manifest.permission.READ_PHONE_STATE, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_SMS
        )
        if (EasyPermissions.hasPermissions(this, *permis)) {
            JLog.r("Permission", "Has permissions")
            initData()
        } else {
            JLog.r("Permission", "Not has Permissions")
            EasyPermissions.requestPermissions(this, "请允许所有权限", 127, *permis)
        }
    }


    override fun onPermissionsGranted(requestCode: Int, perms: List<String>) {
        Log.i("getPermission", "onPermissionsGranted")
        //如果用户点击永远禁止，这个时候就需要跳到系统设置页面去手动打开了
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            AppSettingsDialog.Builder(this).build().show()
        }
    }

    override fun onPermissionsDenied(requestCode: Int, perms: List<String?>) {
        Log.i("getPermission", "onPermissionsDenied")
        getPermission()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            SystemCtrlUtil.WRITE_SETTING -> {
                startAp()
            }
        }
    }

    override fun onDestroy() {
        httpWeb?.stop()
        httpServer?.stop()
        WifiApControl.getInstance().release()
        handler.removeCallbacks(getSpeedRunnable)
        FrpUtil.stopFrp()
        TetheringUtil.stopTetheringService()
        super.onDestroy()
    }

    /**
     * 接收LocalSocket发送数据
     */
    override fun onReceive(clientTransceiver: LocalClientTransceiver, data: String) {
        val cmd = JSONObject(data).getInt("cmd")
        when (cmd) {
            LocalCmd.PACKAGE_INFO -> {//发送包名
                packageInfo(clientTransceiver, data)
            }
        }
    }

    override fun onDisconnect(clientTransceiver: LocalClientTransceiver) {
        lifecycleScope.launch {
            clientTransceiver.packageInfo?.let {
                KLog.i("disconnect : $it")
                val app = it.toCompat()
                app.isRun = false
                AppDatabase.instance.appDao.update(app)
            }
        }
    }

    private fun packageInfo(clientTransceiver: LocalClientTransceiver, data: String) {
        //存入数据库，并且记录
        val cmdBean = GsonUtil.getGsonObject<BaseLocalCmd<PackageInfo>>(data)
        clientTransceiver.packageInfo = cmdBean?.data
        val appInfo = cmdBean?.data?.toCompat()
        lifecycleScope.launch {
            appInfo?.let {
                AppDatabase.instance.appDao.update(it)
            }
            KLog.i("cmdBean : $cmdBean")
        }
    }

    private val getSpeedRunnable = object : Runnable {
        override fun run() {
            val speedArray = WifiApControl.getInstance().getNetSpeed() //下载上传
            val numClients = WifiApControl.getInstance().getNumClients()
            val rx = Formatter.formatFileSize(this@MainActivity, speedArray[0])
            val tx = Formatter.formatFileSize(this@MainActivity, speedArray[1])
            textSpeed?.text = "下载：$rx/s , 上传：$tx/s \n Clients : $numClients"
            handler.postDelayed(this, 2000)
        }
    }
}
