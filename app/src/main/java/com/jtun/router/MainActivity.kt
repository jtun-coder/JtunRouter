package com.jtun.router

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.format.Formatter
import android.util.Log
import android.view.View
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
import com.jtun.router.control.WifiApControl
import com.jtun.router.dialog.ConfirmDialog
import com.jtun.router.dialog.OnConfirmListener
import com.jtun.router.http.HttpServer
import com.jtun.router.http.HttpWebServer
import com.jtun.router.net.wifi.SoftApConfigurationCompat
import com.jtun.router.room.AppDatabase
import com.jtun.router.room.AppInfo.Companion.toCompat
import com.jtun.router.socket.SocketIO
import com.jtun.router.util.DeviceUtil
import com.jtun.router.util.FileHelper
import com.jtun.router.util.JLog
import com.jtun.router.util.KLog
import com.jtun.router.util.NetworkUtils
import com.jtun.router.util.SmsWriteOpUtil
import com.jtun.router.util.SystemCtrlUtil
import com.jtun.router.util.TetheringUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Runnable
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions
import kotlin.system.exitProcess


class MainActivity : AppCompatActivity(), EasyPermissions.PermissionCallbacks, LocalServerListener {
    private var httpWeb: HttpWebServer? = null
    private var httpServer: HttpServer? = null
    private val handler = Handler(Looper.getMainLooper())
    private var textClient: TextView? = null
    private var textConnected: TextView? = null
    private var textUploads: TextView? = null
    private var textDownloads: TextView? = null
    private var textName: TextView? = null
    private var textPassword: TextView? = null
    private var textWeb: TextView? = null
    private var textDeviceId: TextView? = null
    private var rebootDialog:ConfirmDialog? =null
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()
        getPermission()
    }
    private fun initView(){
        textClient = findViewById(R.id.text_clients)
        textConnected = findViewById(R.id.text_connect)
        textUploads = findViewById(R.id.text_uploads)
        textDownloads = findViewById(R.id.text_download)
        textName = findViewById(R.id.text_name)
        textPassword = findViewById(R.id.text_password)
        textWeb = findViewById(R.id.text_web)
        textDeviceId = findViewById(R.id.text_device_id)
        textConnected?.text = WifiApControl.getInstance().sysRealtime()
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
        WifiApControl.getInstance().activeCallback = {
            lifecycleScope.launch {
                val config = WifiApControl.getInstance().getSoftApConfig()
                val ip = NetworkUtils.getLocalIpv4Address()
                withContext(Dispatchers.Main) {
                    setWifiText(config,ip)
                }
            }
        }
        FileHelper.init(this@MainActivity)
        startAp()
        SystemCtrlUtil.systemSettings(this)
        SocketIO.getInstance().connect()
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
            val config = WifiApControl.getInstance().getSoftApConfig()
            val ip = NetworkUtils.getLocalIpv4Address()
            withContext(Dispatchers.Main) {
                setWifiText(config,ip)
                if (!SmsWriteOpUtil.isWriteEnabled(applicationContext)) {
                    KLog.i("isWriteEnabled")
                    SmsWriteOpUtil.setWriteEnabled(applicationContext, true);
                }
                TetheringUtil.startTetheringService()
            }
            SystemCtrlUtil.requestUsageStatsPermission(this@MainActivity)
            handler.postDelayed(getSpeedRunnable, 1000)
        }
    }
    private fun setWifiText(config : SoftApConfigurationCompat?, ip:String){
        textWeb?.text = "http://$ip:2000"
        textDeviceId?.text = DeviceUtil.getDeviceSerial()
        config?.let {
            textName?.text = it.ssid.toString()
            textPassword?.text = it.passphrase
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
        TetheringUtil.stopTetheringService()
        LocalServer.instance.release()
        super.onDestroy()
        exitProcess(0)
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
            textClient?.text = "$numClients"
            textUploads?.text = "$tx/s"
            textDownloads?.text = "$rx/s"
            textConnected?.text = WifiApControl.getInstance().sysRealtime()
            handler.postDelayed(this, 1000)
        }
    }
    fun reboot(view: View){
        if(rebootDialog == null){
            rebootDialog = ConfirmDialog(this,object : OnConfirmListener{
                override fun onConfirm(msg: String?) {
                    App.app.reboot()
                }

                override fun onCancel() {
                }
            })
        }
        rebootDialog?.show()
    }
}
