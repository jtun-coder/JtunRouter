package com.jtun.router.util

import android.app.ActivityManager
import android.content.Context
import android.content.pm.PackageManager
import android.net.wifi.WifiManager
import android.os.Build
import android.os.Environment
import android.os.StatFs
import android.provider.Settings
import androidx.core.content.edit
import com.jtun.router.App.Companion.app
import com.jtun.router.control.WifiApControl
import java.io.BufferedReader
import java.io.FileReader
import java.io.IOException
import java.net.Inet4Address
import java.net.NetworkInterface
import java.net.SocketException
import java.util.UUID


/**
 * 获取设备信息
 */
object DeviceUtil {
    private const val ID_KEY = "jtun_id"
    val uuid:String = UUID.randomUUID().toString()
    private var deviceId: String
        get() = app.pref.getString(ID_KEY,UUID.randomUUID().toString())!!
        set(value) = app.pref.edit { putString(ID_KEY, value) }
    private val cpuStatsNames = arrayOf(
        "user", "nice", "system", "idle", "iowait",
        "irq", "softirq", "stealstolen", "guest", "guest_nice"
    )
    //获取系统运行内存(RAM)大小：
    fun getRAMTotalMemorySize(context: Context): Long {
        //获得ActivityManager服务的对象
        val mActivityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        //获得MemoryInfo对象
        val memoryInfo = ActivityManager.MemoryInfo()
        //获得系统可用内存，保存在MemoryInfo对象上
        mActivityManager.getMemoryInfo(memoryInfo)
        val memSize = memoryInfo.totalMem
        //字符类型转换
//        String availMemStr = formatterFileSize(context, memSize);
        return memSize
    }

    //获取系统可用运行内存(RAM)大小：
    fun getRAMAvailableMemorySize(context: Context): Long {
        //获得MemoryInfo对象
        val mActivityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val memoryInfo = ActivityManager.MemoryInfo()
        //获得系统可用内存，保存在MemoryInfo对象上
        mActivityManager.getMemoryInfo(memoryInfo)
        val memSize = memoryInfo.availMem
        //字符类型转换
//        String availMemStr = formatterFileSize(context, memSize);
        return memSize
    }
    fun getDeviceSerial():String{
        var serial = ""
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            try {
                //不是系统应用会报错
                serial = Build.getSerial()
            }catch (_:Exception){
            }
            if(serial.isEmpty()){
                WifiApControl.getInstance().getImei()?.let {
                    serial = it
                }
            }
        }
        if(serial.isEmpty() || "unknown" == serial){
            serial = deviceId
        }
        return serial
    }

    //获取系统存储空间（ROM）大小:
    fun getROMTotalSize(context: Context?): Long {
        val path = Environment.getExternalStorageDirectory()
        val stat = StatFs(path.path)
        val blockSize = stat.blockSize.toLong()
        val totalBlocks = stat.blockCount.toLong()
        //        String availMemStr = formatterFileSize(context, blockSize * totalBlocks);
        return blockSize * totalBlocks
    }

    //获取系统可用存储空间（ROM）大小:
    fun getROMAvailableSize(context: Context?): Long {
        val path = Environment.getExternalStorageDirectory()
        val stat = StatFs(path.path)
        val blockSize = stat.blockSize.toLong()
        val availableBlocks = stat.availableBlocks.toLong()
        //        String availMemStr = formatterFileSize(context, blockSize * availableBlocks);
        return blockSize * availableBlocks
    }

    /**
     * 获取当前本地apk的版本
     *
     * @param mContext
     * @return
     */
    fun getVersionCode(mContext: Context): Int {
        var versionCode = 0
        try {
            versionCode = mContext.packageManager.getPackageInfo(mContext.packageName,0).versionCode
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return versionCode
    }

    /**
     * 获取当前本地apk的版本
     *
     * @param mContext
     * @return
     */
    fun getVersionName(mContext: Context): String {
        var versionName = ""
        try {
            versionName = mContext.packageManager.getPackageInfo(mContext.packageName, 0).versionName
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return versionName
    }
    fun getCpuTemp():Float{
        try {
            val filePath = "/sys/class/thermal/thermal_zone0/temp"
            val reader = BufferedReader(FileReader(filePath))
            val temperature = reader.readLine()
            reader.close()

            val tempValue = temperature.toFloat() / 1000.0f // 转换为摄氏度
            KLog.i("CPU温度：" + tempValue + "摄氏度")
            return tempValue
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return 20f
    }

    fun getCpuUsage(): Float {
        var rate = 0F
        try {
//            var result: String?
//            val p = Runtime.getRuntime().exec("top -n 1")
//            val br = BufferedReader(InputStreamReader(p.inputStream))
//            while ((br.readLine().also { result = it }) != null) {
//                // replace "com.example.fs" by your application
//                val info = result!!.trim { it <= ' ' }.replace(" +".toRegex(), " ").split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
//                KLog.i("info : ${info.contentToString()}")
//            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return rate
    }
    fun getRamUsed(context: Context):Float{
        val ram = getRAMTotalMemorySize(context)
        val ramAvailable = getRAMAvailableMemorySize(context)
        KLog.i("ram $ram, ramAvailable $ramAvailable")
        return (ramAvailable.toFloat()/ram.toFloat()) * 100
    }
    fun getAndroidId(context: Context):String{
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID)
    }

    /**
     * 获取wifi模块的mac地址，即使wifi是关闭的，需要添加权限 ACCESS_WIFI_STATE
     *
     * @param context
     * @return
     */
    fun getWifiMac(context: Context): String {
        val wifi = context.getSystemService(Context.WIFI_SERVICE) as WifiManager
        val info = wifi.connectionInfo
        val mac = info.macAddress
        return mac
    }

    /**
     * 获取当前使用网络的MAC
     *
     * @return 当前网络MAC
     */
    fun getMac(): String {
        var mac: ByteArray? = null
        val sb = StringBuffer()
        try {
            val netInterfaces = NetworkInterface.getNetworkInterfaces()
            while (netInterfaces.hasMoreElements()) {
                val ni = netInterfaces.nextElement()
                val address = ni.inetAddresses

                while (address.hasMoreElements()) {
                    val ip = address.nextElement()
                    if (ip.isAnyLocalAddress || ip !is Inet4Address || ip.isLoopbackAddress()) continue
//                    KLog.i("getMac ip ${ip.hostAddress} mac: ${ni.hardwareAddress.contentToString()}")
                    if (ip.isSiteLocalAddress()) mac = ni.hardwareAddress
                    else if (!ip.isLinkLocalAddress()) {
                        mac = ni.hardwareAddress
                        break
                    }
                }
            }
        } catch (e: SocketException) {
            e.printStackTrace()
        }

        if (mac != null) {
            KLog.i("getMac ${mac.contentToString()}")
            for (i in mac.indices) {
                sb.append(parseByte(mac[i]))
            }
            return sb.substring(0, sb.length - 1)
        }
        return ""
    }

    // 获取当前连接网络的网卡的mac地址
    private fun parseByte(b: Byte): String {
        val s = "00" + Integer.toHexString(b.toInt()) + ":"
        return s.substring(s.length - 3)
    }

}