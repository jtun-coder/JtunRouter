package com.jtun.router.socket

import android.os.Build
import android.os.SystemClock
import com.google.gson.Gson
import com.jtun.router.App
import com.jtun.router.config.CMD
import com.jtun.router.config.ServerConfig
import com.jtun.router.control.UpdateControl
import com.jtun.router.control.WifiApControl
import com.jtun.router.http.ApConfig
import com.jtun.router.http.HttpServer
import com.jtun.router.http.response.DeviceInfo
import com.jtun.router.http.response.SocketDeviceInfo
import com.jtun.router.util.DeviceUtil
import com.jtun.router.util.KLog
import com.jtun.router.util.NetworkUtils
import io.socket.client.Ack
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.engineio.client.transports.WebSocket
import kotlinx.coroutines.runBlocking
import org.json.JSONObject

class SocketIO private constructor(){
    private val error = "error"
    private val cmd = "cmd"
    private val info = "info"
    private val deviceInfo = "deviceInfo"

    private var socket: Socket? = null
    private var isConnected = false //链接状态
    private var disconnectTime: Long = 0 //断开连接的时间戳
    companion object {
        @Volatile private var instance: SocketIO? = null

        fun getInstance(): SocketIO {
            return instance ?: synchronized(this) {
                instance ?: SocketIO().also { instance = it }
            }
        }
    }

    /**
     * 连接
     */
    fun connect() {
        startExecutor()
    }

    private fun startExecutor(){
        val auth: MutableMap<String, String> = HashMap()
        auth["serial"] = DeviceUtil.getDeviceSerial()
        val options = IO.Options.builder()
            .setAuth(auth)
            .setPath("/im/socket.io")
            .setTransports(arrayOf(WebSocket.NAME))
            .build()
        disconnectTime = System.currentTimeMillis()
        //	设置环节，IO.socket（）里的内容是你服务器端的地址以及监听的端口
//            socket = IO.socket("ws://data.lxyu.com/adserver", options);
        //注意该地址如果是http，需要配置http适配
        socket = IO.socket(ServerConfig.SOCKET_IO_ADDRESS, options)
        socket?.on(Socket.EVENT_CONNECT) { args: Array<Any?> ->
            isConnected = true
            //按照业务逻辑发送数据
            KLog.i("SocketIO", "EVENT_CONNECT")
            sendInfo()
        }?.on(Socket.EVENT_CONNECT_ERROR) { args: Array<Any?> ->
            isConnected = false
            KLog.i("SocketIO", "EVENT_CONNECT_ERROR : " + args[0].toString())
        }?.on(Socket.EVENT_DISCONNECT) { args: Array<Any?> ->
            KLog.i("SocketIO", "EVENT_DISCONNECT")
            isConnected = false
            disconnectTime = System.currentTimeMillis()
        }?.on(error) { args: Array<Any?> ->
            KLog.i("SocketIO", "ERROR : " + args[0].toString())
        }?.on(cmd) {args: Array<Any?> ->
            //接收服务器命令
            cmdInit(args)
        }?.on(deviceInfo){args: Array<Any?> ->
            val config = runBlocking {
                WifiApControl.getInstance().getSoftApConfig()
            }
            val deviceInfo = DeviceInfo()
            deviceInfo.iccid = WifiApControl.getInstance().getIccid()
            deviceInfo.imei = WifiApControl.getInstance().getImei()
            deviceInfo.linkTime = HttpServer.appRuntime
            deviceInfo.wan_ipv4 = NetworkUtils.getLocalIpv4Address()
            deviceInfo.wan_ipv6 = NetworkUtils.getLocalIpv6Address()
            deviceInfo.mac = DeviceUtil.getMac()
            deviceInfo.cpuTemp = DeviceUtil.getCpuTemp()
            deviceInfo.cpuUsage = DeviceUtil.getCpuUsage()
            deviceInfo.memoryUsage = DeviceUtil.getRamUsed(App.app)
            deviceInfo.model = Build.MODEL
            deviceInfo.firmwareVersion = Build.VERSION.RELEASE
            deviceInfo.kernelVersion = DeviceUtil.getVersionName(App.app)
            deviceInfo.uptime = SystemClock.elapsedRealtime()
            deviceInfo.systemTime = System.currentTimeMillis()
            deviceInfo.dataPlan = HttpServer.dataPlan
            deviceInfo.monthUsed = WifiApControl.getInstance().getMonthUsed()

            val clients = runBlocking {
                WifiApControl.getInstance().getConnectedClients()
            }
            val apConfig = ApConfig()
            apConfig.softInitData(config)
            val info = SocketDeviceInfo()
            info.client = clients
            info.device = deviceInfo
            info.apconfig = apConfig

            val json = Gson().toJson(info)
            if (args.size > 1 && args[1] != null && args[1] is Ack) {
                val callback = args[1] as Ack
                callback.call(JSONObject(json))
            }
        }

        //连接
        socket?.connect()
    }
    private fun sendInfo(){
        val config = runBlocking {
            WifiApControl.getInstance().getSoftApConfig()
        }
        val jsonObject = JSONObject()
        jsonObject.put("imei",WifiApControl.getInstance().getImei())
        jsonObject.put("mac",DeviceUtil.getMac())
        jsonObject.put("model",Build.MODEL)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            jsonObject.put("firmware",Build.VERSION.RELEASE_OR_CODENAME)
        }
        jsonObject.put("version",DeviceUtil.getVersionName(App.app))
        jsonObject.put("ssid",config?.ssid)
        jsonObject.put("updateAt",System.currentTimeMillis())
        jsonObject.put("uuid",DeviceUtil.uuid)
        jsonObject.put("iccid",WifiApControl.getInstance().getIccid())
        jsonObject.put("simType",WifiApControl.getInstance().getSimType())
        send(info,jsonObject)
    }

    private fun cmdInit(args: Array<Any?>){
        try {
            val response = args[0] as JSONObject
            KLog.i("cmd : $response")
            val type = response.getInt("type")
            val resultJson = JSONObject()
            resultJson.put("code",0)
            when(type){
                CMD.UPDATE_APK ->{
                    //安装apk
                    UpdateControl.getInstance().downloadApk(response.optString("url",""))
                }
                CMD.UPDATE_OS ->{

                }
                CMD.RESTART ->{
                    App.app.reboot()
                }
                CMD.SHUTDOWN ->{
                    App.app.shutdown()
                }
                CMD.RESET ->{
                    App.app.masterClear()
                }
                CMD.PING ->{
                    val pingStr = NetworkUtils.ping(response.optString("host",""))
                    resultJson.put("data",pingStr)
                }
            }
            KLog.i("result: $resultJson")
            if (args.size > 1 && args[1] != null && args[1] is Ack) {
                val callback = args[1] as Ack
                callback.call(resultJson)
            }
        }catch (e:Exception){
            e.printStackTrace()
        }
    }
    private fun send(event: String, jsonObject: JSONObject?) {
        KLog.i("event $event data : $jsonObject")
        if (isConnected) {
            synchronized(this) {
                //发送消息
                socket!!.emit(event, jsonObject, Ack { args: Array<Any?> ->
                    if (args.isNotEmpty() && args[0] != null)
                        KLog.i(args[0].toString())
                })
            }
        }
    }

    private fun send(event: String, jsonObject: JSONObject?, ack: Ack?) {
        if (isConnected) {
            synchronized(this) {
                KLog.i("event : $event")
                //发送消息
                socket!!.emit(event, jsonObject, ack)
            }
        }
    }
}