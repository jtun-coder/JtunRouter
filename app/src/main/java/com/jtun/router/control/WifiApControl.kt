package com.jtun.router.control

import android.annotation.SuppressLint
import android.app.usage.NetworkStatsManager
import android.content.Context
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.TrafficStats
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import android.telephony.TelephonyManager
import android.text.TextUtils
import androidx.appcompat.app.AppCompatActivity
import com.jtun.router.callback.AppNetworkCallback
import com.jtun.router.http.response.InternetInfo
import com.jtun.router.net.TetheringManager
import com.jtun.router.net.TetheringManager.localOnlyTetheredIfaces
import com.jtun.router.net.TetheringManager.tetheredIfaces
import com.jtun.router.net.wifi.SoftApConfigurationCompat
import com.jtun.router.net.wifi.SoftApConfigurationCompat.Companion.toCompat
import com.jtun.router.net.wifi.WifiApManager
import com.jtun.router.room.ClientConnected
import com.jtun.router.root.RootManager
import com.jtun.router.root.WifiApCommands
import com.jtun.router.util.DeviceUtil
import com.jtun.router.util.JLog
import com.jtun.router.util.KLog
import com.jtun.router.util.NetworkUtils
import com.jtun.router.util.broadcastReceiver
import com.jtun.router.util.getRootCause
import com.jtun.router.widget.SmartSnackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.lang.reflect.InvocationTargetException
import java.net.NetworkInterface
import java.net.SocketException
import java.text.SimpleDateFormat
import java.util.Calendar
import kotlin.coroutines.cancellation.CancellationException


class WifiApControl private constructor() {
    private var context : Context? = null
    private var lastTotalRxBytes: Long = 0
    private var lastTotalTxBytes: Long = 0
    private var lastTimeStamp: Long = 0
    private var networkService : ConnectivityManager? = null
    private val networkCallBack = AppNetworkCallback()
    private val softApControl = SoftApControl()
    private var mTelephonyManager: TelephonyManager? = null
    private var activeIfaces = emptyList<String>() //通过广播接收更新信息
    private var localOnlyIfaces = emptyList<String>()
    private var erroredIfaces = emptyList<String>()
    private var ifaceLookup: Map<String, NetworkInterface> = emptyMap()
    private val receiver = broadcastReceiver { _, intent ->
        activeIfaces = intent.tetheredIfaces ?: return@broadcastReceiver
        localOnlyIfaces = intent.localOnlyTetheredIfaces ?: return@broadcastReceiver
        erroredIfaces = intent.getStringArrayListExtra(TetheringManager.EXTRA_ERRORED_TETHER)
            ?: return@broadcastReceiver
        ifaceLookup = try {
            NetworkInterface.getNetworkInterfaces().asSequence().associateBy { it.name }
        } catch (e: Exception) {
            if (e is SocketException) Timber.d(e) else Timber.w(e)
            emptyMap()
        }
        KLog.i("activeIfaces $activeIfaces")
        KLog.i("ifaceLookup $ifaceLookup")
    }
    companion object {
        @SuppressLint("StaticFieldLeak")
        @Volatile private var instance: WifiApControl? = null
        fun getInstance(): WifiApControl {
            return instance ?: synchronized(this) {
                instance ?: WifiApControl().also { instance = it }
            }
        }
    }

    fun init(context: Context){
        this.context = context
        mTelephonyManager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager?
        networkService = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
        networkService?.registerDefaultNetworkCallback(networkCallBack)
        softApControl.start()
        context.registerReceiver(receiver, IntentFilter(TetheringManager.ACTION_TETHER_STATE_CHANGED))
    }
    fun getSimType():String{
        return ""
//        return "usr"
    }
    fun getIccid(): String? {
        try {
            mTelephonyManager?.let {
                return it.simSerialNumber
            }
        }catch (e:Exception){
            e.printStackTrace()
        }
        return ""
//        return "8986032344201254008"//usr测试
    }
    fun getImei():String{
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                mTelephonyManager?.let {
                    if(it.imei.isNotEmpty()){
                       return it.imei
                    }
                }
            }
        }catch (e:Exception){
            e.printStackTrace()
        }
        return DeviceUtil.deviceId
    }

    /**
     * 获取下载流量
     */
    private fun getTotalRxBytes(): Long {
        try {
            return if (TrafficStats.getUidRxBytes(context!!.applicationInfo.uid) == -1L) 0L else TrafficStats.getMobileRxBytes() / 1024
        }catch (e:Exception){
            e.printStackTrace()
        }
        return 0
    }

    /**
     * 获取上传流量
     */
    private fun getTotalTxBytes(): Long {
        try {
            return if (TrafficStats.getUidTxBytes(context!!.applicationInfo.uid) == -1L) 0L else TrafficStats.getMobileTxBytes() / 1024
        }catch (e:Exception){
            e.printStackTrace()
        }
        return 0
    }

    fun initNetSpeed(){
        this.lastTotalRxBytes = getTotalRxBytes()
        this.lastTotalTxBytes = getTotalTxBytes()
        this.lastTimeStamp = SystemClock.uptimeMillis()
    }
    fun getNetSpeed(): Array<Long> {
        val totalRxBytes = getTotalRxBytes() //下载流量
        val totalTxBytes = getTotalTxBytes() //上传流量
        if (this.lastTotalRxBytes == 0L) {
            this.lastTotalRxBytes = totalRxBytes
        }
        if (this.lastTotalTxBytes == 0L) {
            this.lastTotalTxBytes = totalTxBytes
        }
        var rx = 0L
        var tx = 0L
        val uptimeMillis = SystemClock.uptimeMillis()
        if(uptimeMillis > lastTimeStamp){
            rx = ((totalRxBytes - lastTotalRxBytes) * 1000) / (uptimeMillis - lastTimeStamp)
            tx = ((totalTxBytes - this.lastTotalTxBytes) * 1000) / (uptimeMillis - lastTimeStamp)
            this.lastTimeStamp = uptimeMillis
            this.lastTotalRxBytes = totalRxBytes
            this.lastTotalTxBytes = totalTxBytes
        }
        val speed = arrayOf(rx,tx)
        return speed
    }
    /**
     * 获取已连接过的客户端列表
     */
    suspend fun getConnectedClients(): List<ClientConnected> {
       return softApControl.getConnectedClients()
    }
    fun release(){
        context?.unregisterReceiver(receiver)
        softApControl.stop()
        networkService?.unregisterNetworkCallback(networkCallBack)
    }

    /**
     * 获取流量使用情况
     * samsung error Network stats history of uid -1 is forbidden for caller 10289
     */
    fun trafficStats(){
        try {
            val valueOf = 0L
            val networkStatsManager = context!!.getSystemService(AppCompatActivity.NETWORK_STATS_SERVICE) as NetworkStatsManager
            val calendar = Calendar.getInstance()
            calendar[calendar[1], calendar[2], calendar[5], 0, 0] = 0
            calendar[5] = calendar.getActualMinimum(5)
            KLog.i("valueOf $valueOf ,calendar.timeInMillis:${calendar.timeInMillis} ,System.currentTimeMillis() ${System.currentTimeMillis()}")
            val querySummaryForDevice =
                if (valueOf > calendar.timeInMillis)
                    networkStatsManager.querySummaryForDevice(0, null, valueOf, System.currentTimeMillis())
                else
                    networkStatsManager.querySummaryForDevice(0, null, calendar.timeInMillis, System.currentTimeMillis())
            val rxBytes = ((querySummaryForDevice.rxBytes + querySummaryForDevice.txBytes) / 1024) / 1024
            val calendar2 = Calendar.getInstance()
            calendar2[calendar2[1], calendar2[2], calendar2[5], 0, 0] = 0
            KLog.i("valueOf $valueOf ,calendar.timeInMillis:${calendar2.timeInMillis} ,System.currentTimeMillis() ${System.currentTimeMillis()}")
            val querySummaryForDevice2 =
                if (valueOf > calendar2.timeInMillis)
                    networkStatsManager.querySummaryForDevice(0, null, valueOf, System.currentTimeMillis())
                else
                    networkStatsManager.querySummaryForDevice(0, null, calendar2.timeInMillis, System.currentTimeMillis())
            val rxBytes2 = ((querySummaryForDevice2.rxBytes + querySummaryForDevice2.txBytes) / 1024) / 1024
            val querySummaryForDevice3 = networkStatsManager.querySummaryForDevice(0, null, valueOf, System.currentTimeMillis())
            val rxBytes3 = ((querySummaryForDevice3.rxBytes + querySummaryForDevice3.txBytes) / 1024) / 1024
            KLog.d("updateTracfficStats todayMB:" + rxBytes2 + " monthMB:" + rxBytes + " allMB:" + rxBytes3 + "  startTime:" + SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(valueOf))
        }catch (e : Exception){
            e.printStackTrace()
        }
    }
    fun getAllUsed():Long{
        try {
            val networkStatsManager = context!!.getSystemService(AppCompatActivity.NETWORK_STATS_SERVICE) as NetworkStatsManager
            val querySummaryForDevice = networkStatsManager.querySummaryForDevice(0, null, 0, System.currentTimeMillis())
            val rxBytes3 = querySummaryForDevice.rxBytes + querySummaryForDevice.txBytes
            return rxBytes3
        }catch (e:Exception){
            e.printStackTrace()
        }
        return 0
    }
    fun getTodayUsed() : Long{
        try {
            val networkStatsManager = context!!.getSystemService(AppCompatActivity.NETWORK_STATS_SERVICE) as NetworkStatsManager
            val calendar = Calendar.getInstance()
            calendar[calendar[1], calendar[2], calendar[5], 0, 0] = 0
            val querySummaryForDevice = networkStatsManager.querySummaryForDevice(0, null, calendar.timeInMillis, System.currentTimeMillis())
            val rxBytes = querySummaryForDevice.rxBytes + querySummaryForDevice.txBytes
            return rxBytes
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return 0
    }

    fun getMonthUsed() : Long{
        try {
            val networkStatsManager = context!!.getSystemService(AppCompatActivity.NETWORK_STATS_SERVICE) as NetworkStatsManager
            val calendar = Calendar.getInstance()
            calendar[calendar[1], calendar[2], calendar[5], 0, 0] = 0
            calendar[5] = calendar.getActualMinimum(5)
            val querySummaryForDevice = networkStatsManager.querySummaryForDevice(0, null, calendar.timeInMillis, System.currentTimeMillis())
            val rxBytes = querySummaryForDevice.rxBytes + querySummaryForDevice.txBytes
            return rxBytes
        }catch (e:Exception){
            e.printStackTrace()
        }
        return 0
    }

    fun sysRealtime(){
        // 获取系统运行时间
        val uptimeMillis = SystemClock.elapsedRealtime()
        val minutes = uptimeMillis / (1000 * 60) % 60
        val hours = uptimeMillis / (1000 * 60 * 60) % 24
        val days = uptimeMillis / (1000 * 60 * 60 * 24)
        // 打印系统运行时间
        KLog.i("SystemTime", "Days: $days Hours: $hours Minutes: $minutes")
    }

    /**
     * 获取当前正在连接数量
     */
    fun getNumClients():Int{
        return softApControl.getNumClients()
    }
    fun getInternetInfo():InternetInfo{
        return NetworkUtils.getCellInfo(context)
    }
    suspend fun setAutoShutdown(boolean: Boolean){
        val configuration = getSoftApConfig()
        configuration?.let {
            it.isAutoShutdownEnabled = boolean
            setApConfig(it)
        }
    }
    suspend fun setApConfig(configuration:SoftApConfigurationCompat) {
        //把修改好的数据修改到系统中
        if (Build.VERSION.SDK_INT < 30) @Suppress("DEPRECATION") {
            val wc = configuration.toWifiConfiguration()
            try {
                WifiApManager.setConfiguration(wc)
            } catch (e: InvocationTargetException) {
                try {
                    RootManager.use { it.execute(WifiApCommands.SetConfigurationLegacy(wc)) }
                        .value
                } catch (e: CancellationException) {
                    e.printStackTrace()
                    return
                } catch (eRoot: Exception) {
                    eRoot.addSuppressed(e)
                    e.printStackTrace()
                    return
                }
            }
        } else {
            val platform = try {
                configuration.toPlatform()
            } catch (e: InvocationTargetException) {
                e.printStackTrace()
                return
            }
            try {
                WifiApManager.setConfiguration(platform)
            } catch (e: InvocationTargetException) {
                try {
                    RootManager.use { it.execute(WifiApCommands.SetConfiguration(platform)) }
                        .value
                } catch (e: CancellationException) {
                    e.printStackTrace()
                    return
                } catch (eRoot: Exception) {
                    eRoot.addSuppressed(e)
                    e.printStackTrace()
                    return
                }
            }
        }
    }
    suspend fun getSoftApConfig(): SoftApConfigurationCompat? {
            val configuration = try {
                if (Build.VERSION.SDK_INT < 30) @Suppress("DEPRECATION") {
                    WifiApManager.configurationLegacy?.toCompat() ?: SoftApConfigurationCompat()
                } else WifiApManager.configuration.toCompat()
            } catch (e: InvocationTargetException) {
                if (e.targetException !is SecurityException) Timber.w(e)
                try {
                    if (Build.VERSION.SDK_INT < 30) @Suppress("DEPRECATION") {
                        RootManager.use { it.execute(WifiApCommands.GetConfigurationLegacy()) }?.toCompat()
                            ?: SoftApConfigurationCompat()
                    } else RootManager.use { it.execute(WifiApCommands.GetConfiguration()) }.toCompat()
                } catch (_: CancellationException) {
                    return null
                } catch (eRoot: Exception) {
                    eRoot.addSuppressed(e)
                    if (Build.VERSION.SDK_INT >= 29 || eRoot.getRootCause() !is SecurityException) {
                        Timber.w(eRoot)
                    }
                    SmartSnackbar.make(eRoot).show()
                    return null
                }
            } catch (e: IllegalArgumentException) {
                Timber.w(e)
                SmartSnackbar.make(e).show()
                return null
            }
            KLog.i("soft ap : $configuration")
            return configuration
    }
    fun restartTethering(call:(() -> Unit)?){
        GlobalScope.launch(Dispatchers.Main) {
            TetheringManager.stopTethering(TetheringManager.TETHERING_WIFI,errorCallback = {
                JLog.r("restartTether","restartTethering stop exception : $it")
            }, successCallback = {
                JLog.r("restartTether","restartTethering stop success")
            })
            withContext(Dispatchers.IO){
                delay(5000)
                withContext(Dispatchers.Main){
                    TetheringManager.startTethering(TetheringManager.TETHERING_WIFI,true,object : TetheringManager.StartTetheringCallback{
                        override fun onTetheringStarted() {
                            JLog.r("restartTether","restartTethering startTethering success")
                            call?.invoke()
                        }

                        override fun onTetheringFailed(error: Int?) {
                            JLog.r("restartTether","restartTethering onTetheringFailed error : $error")
                            call?.invoke()
                        }

                        override fun onException(e: Exception) {
                            JLog.r("restartTether","restartTethering startTethering onException : $e")
                        }
                    })
                }
            }
        }
    }
    fun startTethering(call:() -> Unit){
        val handler = Handler(Looper.getMainLooper())
        handler.post {
            TetheringManager.startTethering(TetheringManager.TETHERING_WIFI,true,object : TetheringManager.StartTetheringCallback{
                override fun onTetheringStarted() {
                    JLog.r("startTether","startTethering success")
                    call.invoke()
                }

                override fun onTetheringFailed(error: Int?) {
                    JLog.r("startTether","onTetheringFailed error : $error")
                    call.invoke()
                }

                override fun onException(e: Exception) {
                    JLog.r("startTether","startTethering onException : $e")
                }
            })
        }
    }
}