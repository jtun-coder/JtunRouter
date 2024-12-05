package com.jtun.router.http

import android.content.ComponentName
import android.net.MacAddress
import android.os.Build
import android.os.SystemClock
import android.text.TextUtils
import androidx.annotation.RequiresApi
import androidx.core.content.edit
import com.dolphin.localsocket.cmd.BaseLocalCmd
import com.dolphin.localsocket.cmd.LocalCmd
import com.dolphin.localsocket.server.LocalServer
import com.dolphin.localsocket.utils.GsonUtil
import com.google.common.net.HttpHeaders
import com.google.gson.Gson
import com.jtun.router.App
import com.jtun.router.config.Config
import com.jtun.router.config.HttpInterface
import com.jtun.router.control.AppStoreControl
import com.jtun.router.control.WifiApControl
import com.jtun.router.http.response.BaseResponse
import com.jtun.router.http.response.DeviceInfo
import com.jtun.router.http.response.NetSpeed
import com.jtun.router.http.response.openvpn.OpenvpnItem
import com.jtun.router.http.response.UsedDataInfo
import com.jtun.router.http.response.frp.FrpConfig
import com.jtun.router.http.response.v2ray.Configs
import com.jtun.router.http.response.v2ray.ServersCache
import com.jtun.router.room.AppDatabase
import com.jtun.router.room.AppInfo
import com.jtun.router.room.ClientConnected
import com.jtun.router.sms.SmsManager
import com.jtun.router.util.ApkUtils
import com.jtun.router.util.DeviceUtil
import com.jtun.router.util.JLog
import com.jtun.router.util.KLog
import com.jtun.router.util.NetworkUtils
import com.jtun.router.util.getMd5Hash
import fi.iki.elonen.NanoHTTPD
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.json.JSONObject


/**
 * 接口服务提供web的数据支持
 */
class HttpServer : NanoHTTPD(Config.HTTP_PORT) {
    private val tag = HttpServer::class.java.simpleName
    companion object{
        private const val KEY_ADMIN_PASS = "admin_pass_key"
        private const val DATA_PLAN = "data_plan"
        var dataPlan : Long
            get() = App.app.pref.getLong(DATA_PLAN,0L)
            set(value) = App.app.pref.edit { putLong(DATA_PLAN,value) }
        val appRuntime = System.currentTimeMillis()
    }
    private var adminPass: String?
        get() = App.app.pref.getString(KEY_ADMIN_PASS, getMd5Hash("12345678"))
        set(value) = App.app.pref.edit { putString(KEY_ADMIN_PASS, value) }

    override fun serve(iHTTPSession: IHTTPSession?): Response {
        val uri = iHTTPSession?.uri
        val method = iHTTPSession?.method
        val hashMap = HashMap<String,String>()
        KLog.i("uri : $uri  method : $method")
        var response : Response? = null
        //校验密码
        //判断当前请求没有token并且不为登录，返回missing token 错误
        if(Method.POST == method){
            try {
                val contentType = iHTTPSession.headers["content-type"]
                iHTTPSession.parseBody(hashMap)
                val postData = hashMap["postData"]
                val parms = iHTTPSession.parms
                contentType?.let {
                    if(contentType.startsWith("multipart/form-data")){
                        response = analysisFileDownload(parms,hashMap)
                    }
                    if(contentType.startsWith("application/json")){
                        response = analysisPostData(uri,postData)
                    }
                }
            }catch (e :Exception){
                e.printStackTrace()
                JLog.e(tag,"error:" + e.message)
            }
        }
        if(response == null){
            return getStringResponse("")
        }
        return response!!
    }
    private fun analysisPostData(uri:String?, str: String?):Response{
        KLog.i("post request data : $str")
        JLog.r(tag,"post request uri： $uri ,data :  $str")
        var resp = BaseResponse(404,"not found")
        //解析请求命令
        val jsonObject = str?.let { JSONObject(it) }
        val pass = jsonObject?.optString("password")
        if(pass != null){
            //校验密码
            if(pass != adminPass && pass != getMd5Hash(DeviceUtil.uuid)){
                JLog.o(tag,"password error: $pass")
                return getStringResponse(BaseResponse(500,"password error").toJsonString())
            }
            try {
                uri?.let {
                    when(it){
                        HttpInterface.LOGIN -> {
                            resp = login()
                        }
                        HttpInterface.NET_STATE ->{
                            resp = netState()
                        }
                        HttpInterface.DEVICE_INFO ->{
                            resp = deviceInfo()
                        }
                        HttpInterface.AP_CONFIG ->{
                            resp = getApConfig()
                        }
                        HttpInterface.SET_AP_CONFIG ->{
                            val data = jsonObject.getJSONObject("data")
                            resp = setApConfig(data.toString())
                            WifiApControl.getInstance().restartTethering()
                        }
                        HttpInterface.SET_ADMIN_PASS ->{
                            val newPass = jsonObject.getString("newPass")
                            if(TextUtils.isEmpty(newPass)){
                                resp = BaseResponse(500,"not found newPass")
                            }else{
                                adminPass = newPass
                            }
                            JLog.o(tag,"set password: $newPass")
                            resp = BaseResponse()
                        }
                        HttpInterface.GET_CLIENTS ->{
                            val clients = runBlocking {
                                WifiApControl.getInstance().getConnectedClients()
                            }
                            JLog.o(tag,"get clients: $clients")
                            resp = BaseResponse(data = clients)
                        }
                        HttpInterface.SET_CLIENT ->{
                            val dataJson = jsonObject.getJSONObject("data")
                            val client = Gson().fromJson(dataJson.toString(),ClientConnected::class.java)
                            runBlocking {
                                AppDatabase.instance.clientDao.update(client)
                            }
                            resp = BaseResponse()
                        }
                        HttpInterface.GET_INTERNET_INFO ->{
                            val info = WifiApControl.getInstance().getInternetInfo()
                            JLog.o(tag,"get internet info: $info")
                            resp = BaseResponse(data = info)
                        }
                        HttpInterface.SET_DATA_PLAN ->{
                            dataPlan = jsonObject.getLong("dataPlan")
                            JLog.o(tag,"set data plan: $dataPlan")
                            resp = BaseResponse()
                        }
                        HttpInterface.GET_USED_INFO ->{
                            val usedInfo = UsedDataInfo()
                            usedInfo.initData()
                            JLog.o(tag,"used info : $usedInfo")
                            resp = BaseResponse(data = usedInfo)
                        }
                        HttpInterface.DELETE_CLIENT->{
                            val mac = jsonObject.getString("mac")
                            runBlocking {
                                AppDatabase.instance.clientDao.deleteClientMac(mac)
                            }
                            JLog.o(tag,"delete client : $mac")
                            resp = BaseResponse()
                        }
                        HttpInterface.DELETE_CLIENT_LIST->{
                            val macList = jsonObject.getJSONArray("macList")
                            runBlocking {
                                for (index in 0 until macList.length()){
                                    val mac = macList.getString(index)
                                    AppDatabase.instance.clientDao.deleteClientMac(mac)
                                }
                            }
                            JLog.o(tag,"delete client : $macList")
                            resp = BaseResponse()
                        }
                        HttpInterface.GET_APP_LIST ->{
                            val appList = runBlocking {
                                AppDatabase.instance.appDao.getList()
                            }
                            checkAppOnline(appList)
                            JLog.t(tag,"get app list : $appList")
                            resp = BaseResponse(data = appList)
                        }
                        HttpInterface.START_APP ->{
                            val packageName = jsonObject.getString("packageName")
                            JLog.t(tag,"start app : $packageName")
                            resp = runBlocking {
                                ApkUtils.cmdKillApp(packageName)
                                LocalServer.instance.killClient(packageName)
                                val appInfo = AppDatabase.instance.appDao.lookupOrDefault(packageName)
                                withContext(Dispatchers.Main){
                                    if(appInfo?.canonicalName != null){
                                        val component = ComponentName(appInfo.packageName, appInfo.canonicalName)
                                        ApkUtils.startAct(App.app, component)
                                        BaseResponse()
                                    }else{
                                        JLog.t(tag,"Not found app : $packageName")
                                        BaseResponse(message = "Not found app")
                                    }
                                }
                            }

                        }
                        HttpInterface.STOP_APP->{
                            val packageName = jsonObject.getString("packageName")
                            JLog.t(tag,"stop app : $packageName")
                            resp = runBlocking {
                                val result = ApkUtils.cmdKillApp(packageName)
                                if (result) {
                                    BaseResponse()
                                } else {
                                    JLog.o(tag,"stop app error : $packageName")
                                    BaseResponse(message = "Stop app error")
                                }
                            }
                            LocalServer.instance.killClient(packageName)
                        }
                        HttpInterface.V2RAY_CONFIG->{
                            resp = v2rayGetConfig()
                        }
                        HttpInterface.V2RAY_IMPORT->{
                            val config = jsonObject.getString("config")
                            resp = v2rayImport(config)
                        }
                        HttpInterface.V2RAY_START->{
                            resp = v2rayStart()
                        }
                        HttpInterface.V2RAY_STOP->{
                            resp= v2rayStop()
                        }
                        HttpInterface.V2RAY_CHECK_CONFIG->{
                            resp = v2rayCheck(jsonObject.getString("guid"))
                        }
                        HttpInterface.V2RAY_DELETE_CONFIG->{
                            resp = v2rayDelete(jsonObject.getString("guid"))
                        }
                        HttpInterface.V2RAY_PING_TEST ->{
                            JLog.t(tag,"v2ray ping test")
                            resp = v2rayPing()
                        }
                        HttpInterface.APP_INSTALL ->{
                            val url = jsonObject.getString("url")
                            val appInfo = Gson().fromJson(jsonObject.getJSONObject("appInfo").toString(),AppInfo::class.java)
                            JLog.t(tag,"app install ${appInfo.packageName}")
                            resp = appInstall(url,appInfo)
                        }
                        HttpInterface.APP_UNINSTALL ->{
                            resp = appUninstall(jsonObject.getString("packageName"))
                        }
                        HttpInterface.INSTALL_INFO-> {
                            resp = appInstallInfo(jsonObject.getString("packageName"))
                            KLog.i("install info: ${resp.toJsonString()}")
                        }
                        HttpInterface.SMS_BY_CONTACT ->{
                            resp = smsByContact()
                        }
                        HttpInterface.SMS_LIST ->{
                            resp = smsList(jsonObject.getString("address"))
                        }
                        HttpInterface.SEND_SMS ->{
                            resp = sendSms(jsonObject.getString("address"),jsonObject.getString("body"))
                        }
                        HttpInterface.READ_SMS ->{
                            resp = readSms(jsonObject.getInt("threadId"))
                        }
                        HttpInterface.DELETE_SMS ->{
                            resp = deleteSms(jsonObject.getInt("smsId"))
                        }
                        HttpInterface.DELETE_SMS_CONTACT ->{
                            resp = deleteSmsContact(jsonObject.getInt("threadId"))
                        }
                        HttpInterface.DELETE_SMS_LIST ->{
                            val smsList:MutableList<Int> = mutableListOf()
                            val jsonArray = jsonObject.getJSONArray("smsIdList")
                            for (index in 0 until jsonArray.length()){
                                smsList.add(jsonArray.getInt(index))
                            }
                            resp = deleteSmsList(smsList)
                        }
                        HttpInterface.BLACK_CLIENT ->{
                            val mac = jsonObject.getString("mac")
                            //添加该设备到黑名单
                            JLog.o(tag,"black client: $mac")
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                                resp = blackClient(mac)
                            }
                        }
                        HttpInterface.BLACK_CLIENT_REMOVE ->{
                            val mac = jsonObject.getString("mac")
                            //添加该设备移除黑名单
                            JLog.o(tag,"remove black client: $mac")
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                resp = removeBlackClient(mac)
                            }
                        }
                        HttpInterface.LOG_LIST ->{
                            val type = jsonObject.getInt("type")
                            val limit = jsonObject.getInt("limit")
                            val offset = jsonObject.getInt("offset")
                            JLog.o(tag,"get log list")
                            resp = logList(type,limit,offset)
                        }
                        HttpInterface.REFRESH_LIST_BY_ID ->{
                            val type = jsonObject.getInt("type")
                            val limit = jsonObject.getInt("limit")
                            val id = jsonObject.getLong("id")
                            JLog.o(tag,"get log list")
                            resp = refreshListById(type,limit,id)
                        }
                        HttpInterface.MORE_LIST_BY_ID ->{
                            val type = jsonObject.getInt("type")
                            val limit = jsonObject.getInt("limit")
                            val id = jsonObject.getLong("id")
                            JLog.o(tag,"get log list")
                            resp = moreListById(type,limit,id)
                        }
                        HttpInterface.ALIST_STATE ->{
                            resp = alistState()
                        }
                        HttpInterface.ALIST_START ->{
                            resp = alistStart()
                        }
                        HttpInterface.ALIST_STOP ->{
                            resp = alistStop()
                        }
                        HttpInterface.ALIST_ADMIN_PASS ->{
                            val adminPass = jsonObject.getString("adminPass")
                            resp = alistPass(adminPass)
                        }
                        HttpInterface.OPENVPN_IMPORT ->{
                            val name = jsonObject.getString("name")
                            val config = jsonObject.getString("config")
                            resp = openvpnConfig(name,config)
                        }
                        HttpInterface.OPENVPN_START ->{
                            val uuid = jsonObject.getString("uuid")
                            resp = openvpnStart(uuid)
                        }
                        HttpInterface.OPENVPN_STOP ->{
                            val uuid = jsonObject.getString("uuid")
                            resp = openvpnStop(uuid)
                        }
                        HttpInterface.OPENVPN_LIST ->{
                            resp = openvpnList()
                        }
                        HttpInterface.OPENVPN_REMOVE->{
                            val uuid = jsonObject.getString("uuid")
                            resp = openvpnRemove(uuid)
                        }
                        HttpInterface.FRP_LIST->{
                            resp = frpList()
                        }
                        HttpInterface.FRP_REMOVE->{
                            val uid = jsonObject.getString("uid")
                            resp = frpRemove(uid)
                        }
                        HttpInterface.FRP_ADD->{
                            val config = jsonObject.getString("config")
                            resp = frpImport(config)
                        }
                        HttpInterface.FRP_START->{
                            val uid = jsonObject.getString("uid")
                            resp = frpStart(uid)
                        }
                        HttpInterface.FRP_STOP->{
                            val uid = jsonObject.getString("uid")
                            resp = frpStop(uid)
                        }
                        HttpInterface.FRP_CONFIG->{
                            val uid = jsonObject.getString("uid")
                            resp = frpConfig(uid)
                        }
                        HttpInterface.FRP_EDIT->{
                            val config = jsonObject.getString("config")
                            resp = frpEdit(config)
                        }
                        else -> {
                            resp = BaseResponse(500,"Api not found")
                        }

                    }
                }
            }catch (e : Exception){
                JLog.e(tag,"error:" + e.message)
                resp = BaseResponse(500,"error:" + e.message)
            }
        }else{
            resp = BaseResponse(500,"not found password")
        }
        return getStringResponse(resp.toJsonString())
    }
    @RequiresApi(Build.VERSION_CODES.N)
    private fun removeBlackClient(mac: String):BaseResponse{
        val configuration = runBlocking {
            WifiApControl.getInstance().getSoftApConfig()
        }
        configuration?.let {
            val list = configuration.blockedClientList.toMutableList()
            list.removeIf {
                it.toString() == mac
            }
            configuration.blockedClientList = list.toList()
            KLog.i("configuration removeBlackClient ${configuration.blockedClientList}")
            runBlocking {
                val clientConnected = AppDatabase.instance.clientDao.lookupOrDefault(mac)
                clientConnected.allowInternet = true
                AppDatabase.instance.clientDao.update(clientConnected)
                WifiApControl.getInstance().setApConfig(configuration)
            }
        }
        return BaseResponse()
    }
    private fun moreListById(type:Int,limit:Int,id:Long):BaseResponse{
        val logList = runBlocking {
            AppDatabase.instance.logDao.moreListById(type,limit,id)
        }
        return BaseResponse(data = logList)
    }
    private fun refreshListById(type:Int,limit:Int,id:Long):BaseResponse{
        val logList = runBlocking {
            AppDatabase.instance.logDao.refreshListById(type,limit,id)
        }
        return BaseResponse(data = logList)
    }
    private fun logList(type:Int,limit:Int,offset:Int):BaseResponse{
        val logList = runBlocking {
            AppDatabase.instance.logDao.getList(type,limit,offset)
        }
        return BaseResponse(data = logList)
    }
    @RequiresApi(Build.VERSION_CODES.P)
    private fun blackClient(mac:String):BaseResponse{
        val configuration = runBlocking {
            WifiApControl.getInstance().getSoftApConfig()
        }
        configuration?.let {
            val list = configuration.blockedClientList.toMutableList()
            list.add(MacAddress.fromString(mac))
            configuration.blockedClientList = list.toList()
            KLog.i("configuration blockedClientList ${configuration.blockedClientList}")
            runBlocking {
                val clientConnected = AppDatabase.instance.clientDao.lookupOrDefault(mac)
                clientConnected.allowInternet = false
                AppDatabase.instance.clientDao.update(clientConnected)
                WifiApControl.getInstance().setApConfig(configuration)
            }
        }
        return BaseResponse()
    }
    private fun smsByContact():BaseResponse{
        val smsContact = SmsManager.getLatestSmsByContact(App.app)
        JLog.o(tag,"get sms contact")
        return BaseResponse(data = smsContact)
    }
    private fun sendSms(address: String,body:String):BaseResponse{
        SmsManager.sendSms(address,body)
        JLog.o(tag,"send sms phone $address ,body $body")
        return BaseResponse()
    }
    private fun deleteSmsContact(threadId: Int):BaseResponse{
        val result = SmsManager.deleteSmsByThreadId(App.app,threadId)
        JLog.o(tag,"delete sms contact: $threadId")
        return BaseResponse(data = result)
    }
    private fun deleteSmsList(smsIdList:List<Int>):BaseResponse{
        val result = SmsManager.deleteSmsList(App.app,smsIdList)
        JLog.o(tag,"delete sms list")
        return BaseResponse(data = result)
    }
    private fun deleteSms(smsId:Int):BaseResponse{
        val result = SmsManager.deleteSms(App.app,smsId)
        JLog.o(tag,"delete sms: $smsId")
        return BaseResponse(data = result)
    }
    private fun readSms(threadId:Int):BaseResponse{
        val result = SmsManager.markSmsAsRead(App.app,threadId)
        JLog.o(tag,"read sms")
        return BaseResponse(data = result)
    }
    private fun smsList(phoneNum:String):BaseResponse{
        val smsList = SmsManager.getAddressSms(App.app,phoneNum)
        JLog.o(tag,"get sms list $phoneNum")
        return BaseResponse(data = smsList)
    }
    private fun appInstall(url:String,app:AppInfo):BaseResponse{
        val appState = AppStoreControl.getInstance().createInstallApk(url,app)
        JLog.t(tag,"createInstallApk ${app.packageName}")
        return BaseResponse(data = appState)
    }
    private fun appUninstall(packageName:String):BaseResponse{
        JLog.t(tag,"app uninstall $packageName")
        val appState = AppStoreControl.getInstance().unInstallApk(packageName)
        return BaseResponse(data = appState)
    }
    private fun appInstallInfo(packageName:String):BaseResponse{
        val appState = AppStoreControl.getInstance().getInstallInfo(packageName)
        if(appState != null){
            JLog.t(tag,"app install info $appState")
            return BaseResponse(data = appState)
        }
        JLog.t(tag,"app install not found $packageName info")
        return BaseResponse(500, message = "Not found $packageName info")
    }
    private fun v2rayPing():BaseResponse{
        //1.获取v2ray local socket连接对象
        val client = LocalServer.instance.getClient(Config.V2RAY_PACKAGE_NAME) ?: return BaseResponse(message = "V2ray not start")
        //发送获取getConfig命令
        client.send(BaseLocalCmd<Any?>(LocalCmd.V2RAY_PING_TEST).toString())
        val cmdStr = client.receiveCmd()
        val configCmd = GsonUtil.getGsonObject<BaseLocalCmd<Long>>(cmdStr)
        configCmd?.let {
            return BaseResponse(code = configCmd.value,data = configCmd.data)
        }
        return BaseResponse(500, message = "Failed")
    }
    private fun checkAppOnline(list:List<AppInfo>){
        for (appInfo in list){
            val client = LocalServer.instance.getClient(appInfo.packageName)
            appInfo.isRun = client != null
        }
    }
    private fun v2rayDelete(guid:String):BaseResponse{
        //1.获取v2ray local socket连接对象
        val client = LocalServer.instance.getClient(Config.V2RAY_PACKAGE_NAME) ?: return BaseResponse(message = "V2ray not start")
        //发送获取getConfig命令
        client.send(BaseLocalCmd<Any?>(LocalCmd.V2RAY_DELETE_CONFIG, data = guid).toString())
        val cmdStr = client.receiveCmd()
        val configCmd = GsonUtil.getGsonObject<BaseLocalCmd<Any?>>(cmdStr)
        configCmd?.let {
            JLog.t(tag,"v2ray delete config $cmdStr")
            return BaseResponse(code = configCmd.value,message = it.message)
        }
        JLog.t(tag,"v2ray delete config failed")
        return BaseResponse(500, message = "Failed")
    }
    private fun v2rayCheck(guid:String):BaseResponse{
        //1.获取v2ray local socket连接对象
        val client = LocalServer.instance.getClient(Config.V2RAY_PACKAGE_NAME) ?: return BaseResponse(message = "V2ray not start")
        //发送获取getConfig命令
        client.send(BaseLocalCmd<Any?>(LocalCmd.V2RAY_CHECK_CONFIG, data = guid).toString())
        val cmdStr = client.receiveCmd()
        val configCmd = GsonUtil.getGsonObject<BaseLocalCmd<Any?>>(cmdStr)
        configCmd?.let {
            return BaseResponse(code = configCmd.value)
        }
        return BaseResponse(500, message = "Failed")
    }
    private fun v2rayStop():BaseResponse{
        //1.获取v2ray local socket连接对象
        val client = LocalServer.instance.getClient(Config.V2RAY_PACKAGE_NAME) ?: return BaseResponse(message = "V2ray not start")
        //发送获取getConfig命令
        client.send(BaseLocalCmd<Any?>(LocalCmd.V2RAY_STOP).toString())
        val cmdStr = client.receiveCmd()
        val configCmd = GsonUtil.getGsonObject<BaseLocalCmd<Any?>>(cmdStr)
        configCmd?.let {
            JLog.t(tag,"v2ray stop success $cmdStr")
            return BaseResponse(code = configCmd.value)
        }
        JLog.t(tag,"v2ray stop failed")
        return BaseResponse(500, message = "Failed")
    }
    private fun v2rayStart():BaseResponse{
        //1.获取v2ray local socket连接对象
        val client = LocalServer.instance.getClient(Config.V2RAY_PACKAGE_NAME) ?: return BaseResponse(message = "V2ray not start")
        //发送获取getConfig命令
        client.send(BaseLocalCmd<Any?>(LocalCmd.V2RAY_START).toString())
        val cmdStr = client.receiveCmd()
        val configCmd = GsonUtil.getGsonObject<BaseLocalCmd<Any?>>(cmdStr)
        configCmd?.let {
            JLog.t(tag,"v2ray start success $cmdStr")
            return BaseResponse(code = configCmd.value)
        }
        JLog.t(tag,"v2ray start failed")
        return BaseResponse(500, message = "Failed")
    }
    private fun v2rayImport(config:String):BaseResponse{
        //1.获取v2ray local socket连接对象
        val client = LocalServer.instance.getClient(Config.V2RAY_PACKAGE_NAME) ?: return BaseResponse(message = "V2ray not start")
        //发送获取getConfig命令
        client.send(BaseLocalCmd<Any?>(LocalCmd.V2RAY_IMPORT_CONFIG, data = config).toString())
        val cmdStr = client.receiveCmd()
        val configCmd = GsonUtil.getGsonObject<BaseLocalCmd<Any?>>(cmdStr)
        configCmd?.let {
            if (it.value > 0){
                JLog.t(tag,"v2ray import success $cmdStr")
                return BaseResponse(code = configCmd.value)
            }
        }
        JLog.t(tag,"v2ray import failed")
        return BaseResponse(500, message = "Import failed")
    }
    private fun v2rayGetConfig():BaseResponse{
        //1.获取v2ray local socket连接对象
        val client = LocalServer.instance.getClient(Config.V2RAY_PACKAGE_NAME) ?: return BaseResponse(message = "V2ray not start")
        //发送获取getConfig命令
        client.send(BaseLocalCmd<Any?>(LocalCmd.V2RAY_GET_CONFIG).toString())
        //等待接收返回信息
        val cmdStr = client.receiveCmd()
        val configCmd = GsonUtil.getGsonObject<BaseLocalCmd<List<ServersCache>>>(cmdStr)
        KLog.i("v2rayGetConfig cmdString : $cmdStr")
        JLog.t(tag,"v2rayGetConfig")
        val configs = Configs(configCmd?.message?:"",configCmd?.value == 1,configCmd?.data!!)
        //解析cmd然后返回给请求方
        return BaseResponse(code = configCmd.value, data = configs)
    }
    private fun alistState():BaseResponse{
        //1.获取alist local socket连接对象
        val client = LocalServer.instance.getClient(Config.ALIST_PACKAGE_NAME) ?: return BaseResponse(message = "AList not start")
        //发送获取getConfig命令
        client.send(BaseLocalCmd<Any?>(LocalCmd.ALIST_STATE).toString())
        //等待接收返回信息
        val cmdStr = client.receiveCmd()
        val configCmd = GsonUtil.getGsonObject<BaseLocalCmd<Boolean>>(cmdStr)
        configCmd?.let {
            JLog.t(tag,"AList state $cmdStr")
            return BaseResponse(data = it.data)
        }
        JLog.t(tag,"AList failed")
        return BaseResponse(500, message = "Failed")
    }
    private fun alistPass(pass:String):BaseResponse{
        //1.获取alist local socket连接对象
        val client = LocalServer.instance.getClient(Config.ALIST_PACKAGE_NAME) ?: return BaseResponse(message = "AList not start")
        //发送获取getConfig命令
        client.send(BaseLocalCmd(LocalCmd.ALIST_ADMIN_PASS, data = pass).toString())
        //等待接收返回信息
        val cmdStr = client.receiveCmd()
        val configCmd = GsonUtil.getGsonObject<BaseLocalCmd<Any?>>(cmdStr)
        configCmd?.let {
            JLog.t(tag,"AList admin pass $cmdStr")
            return BaseResponse()
        }
        JLog.t(tag,"AList admin pass failed")
        return BaseResponse(500, message = "Failed")
    }
    private fun alistStart():BaseResponse{
        //1.获取alist local socket连接对象
        val client = LocalServer.instance.getClient(Config.ALIST_PACKAGE_NAME) ?: return BaseResponse(message = "AList not start")
        //发送获取getConfig命令
        client.send(BaseLocalCmd<Any?>(LocalCmd.ALIST_START).toString())
        //等待接收返回信息
        val cmdStr = client.receiveCmd()
        val configCmd = GsonUtil.getGsonObject<BaseLocalCmd<Any?>>(cmdStr)
        configCmd?.let {
            JLog.t(tag,"AList start success $cmdStr")
            return BaseResponse()
        }
        JLog.t(tag,"AList start failed")
        return BaseResponse(500, message = "Failed")
    }
    private fun alistStop():BaseResponse{
        //1.获取alist local socket连接对象
        val client = LocalServer.instance.getClient(Config.ALIST_PACKAGE_NAME) ?: return BaseResponse(message = "AList not start")
        //发送获取getConfig命令
        client.send(BaseLocalCmd<Any?>(LocalCmd.ALIST_STOP).toString())
        //等待接收返回信息
        val cmdStr = client.receiveCmd()
        val configCmd = GsonUtil.getGsonObject<BaseLocalCmd<Any?>>(cmdStr)
        configCmd?.let {
            JLog.t(tag,"AList start success $cmdStr")
            return BaseResponse()
        }
        JLog.t(tag,"AList start failed")
        return BaseResponse(500, message = "Failed")
    }
    private fun openvpnConfig(name:String,config:String):BaseResponse{
        val client = LocalServer.instance.getClient(Config.OPENVPN_PACKAGE_NAME) ?: return BaseResponse(message = "OpenVpn not start")
        client.send(BaseLocalCmd(LocalCmd.OPENVPN_IMPORT,name, data = config).toString())
        val cmdStr = client.receiveCmd()
        val configCmd = GsonUtil.getGsonObject<BaseLocalCmd<Any?>>(cmdStr)
        configCmd?.let {
            JLog.t(tag,"OpenVpn import success $cmdStr")
            return BaseResponse(message = configCmd.message)
        }
        JLog.t(tag,"OpenVpn import failed")
        return BaseResponse(500, message = "Failed")
    }

    private fun openvpnStart(uuid:String):BaseResponse{
        val client = LocalServer.instance.getClient(Config.OPENVPN_PACKAGE_NAME) ?: return BaseResponse(message = "OpenVpn not start")
        client.send(BaseLocalCmd(LocalCmd.OPENVPN_START, data = uuid).toString())
        val cmdStr = client.receiveCmd()
        val configCmd = GsonUtil.getGsonObject<BaseLocalCmd<Any?>>(cmdStr)
        configCmd?.let {
            JLog.t(tag,"OpenVpn start success $cmdStr")
            return BaseResponse(message = configCmd.message)
        }
        JLog.t(tag,"OpenVpn start failed")
        return BaseResponse(500, message = "Failed")
    }
    private fun openvpnStop(uuid:String):BaseResponse{
        val client = LocalServer.instance.getClient(Config.OPENVPN_PACKAGE_NAME) ?: return BaseResponse(message = "OpenVpn not start")
        client.send(BaseLocalCmd(LocalCmd.OPENVPN_STOP, data = uuid).toString())
        val cmdStr = client.receiveCmd()
        val configCmd = GsonUtil.getGsonObject<BaseLocalCmd<Any?>>(cmdStr)
        configCmd?.let {
            JLog.t(tag,"OpenVpn stop success $cmdStr")
            return BaseResponse(message = configCmd.message)
        }
        JLog.t(tag,"OpenVpn stop failed")
        return BaseResponse(500, message = "Failed")
    }
    private fun openvpnList():BaseResponse{
        val client = LocalServer.instance.getClient(Config.OPENVPN_PACKAGE_NAME) ?: return BaseResponse(message = "OpenVpn not start")
        client.send(BaseLocalCmd<Any?>(LocalCmd.OPENVPN_LIST).toString())
        val cmdStr = client.receiveCmd()
        val configCmd = GsonUtil.getGsonObject<BaseLocalCmd<List<OpenvpnItem>>>(cmdStr)
        configCmd?.let {
            JLog.t(tag,"OpenVpn list success $cmdStr")
            return BaseResponse(message = configCmd.message, data = configCmd.data)
        }
        JLog.t(tag,"OpenVpn list failed")
        return BaseResponse(500, message = "Failed")
    }
    private fun openvpnRemove(uuid:String):BaseResponse{
        val client = LocalServer.instance.getClient(Config.OPENVPN_PACKAGE_NAME) ?: return BaseResponse(message = "OpenVpn not start")
        client.send(BaseLocalCmd(LocalCmd.OPENVPN_REMOVE, data = uuid).toString())
        val cmdStr = client.receiveCmd()
        val configCmd = GsonUtil.getGsonObject<BaseLocalCmd<Any?>>(cmdStr)
        configCmd?.let {
            JLog.t(tag,"OpenVpn remove success $cmdStr")
            return BaseResponse(message = configCmd.message)
        }
        JLog.t(tag,"OpenVpn remove failed")
        return BaseResponse(500, message = "Failed")
    }
    private fun frpList():BaseResponse{
        val client = LocalServer.instance.getClient(Config.FRP_PACKAGE_NAME) ?: return BaseResponse(message = "Frp not start")
        client.send(BaseLocalCmd<Any?>(LocalCmd.FRP_LIST).toString())
        val cmdStr = client.receiveCmd()
        val configCmd = GsonUtil.getGsonObject<BaseLocalCmd<List<FrpConfig>>>(cmdStr)
        configCmd?.let {
            JLog.t(tag,"Frp list success $cmdStr")
            return BaseResponse(code = configCmd.value,message = configCmd.message, data = configCmd.data)
        }
        JLog.t(tag,"Frp list failed")
        return BaseResponse(500, message = "Failed")
    }
    private fun frpRemove(uid:String):BaseResponse{
        val client = LocalServer.instance.getClient(Config.FRP_PACKAGE_NAME) ?: return BaseResponse(message = "Frp not start")
        client.send(BaseLocalCmd(LocalCmd.FRP_REMOVE, data = uid).toString())
        val cmdStr = client.receiveCmd()
        val configCmd = GsonUtil.getGsonObject<BaseLocalCmd<Any?>>(cmdStr)
        configCmd?.let {
            JLog.t(tag,"Frp remove success $cmdStr")
            return BaseResponse(code = configCmd.value,message = configCmd.message)
        }
        JLog.t(tag,"Frp remove failed")
        return BaseResponse(500, message = "Failed")
    }
    private fun frpImport(configStr:String):BaseResponse{
        val client = LocalServer.instance.getClient(Config.FRP_PACKAGE_NAME) ?: return BaseResponse(message = "Frp not start")
        client.send(BaseLocalCmd(LocalCmd.FRP_IMPORT, data = configStr).toString())
        val cmdStr = client.receiveCmd()
        val configCmd = GsonUtil.getGsonObject<BaseLocalCmd<Any?>>(cmdStr)
        configCmd?.let {
            JLog.t(tag,"Frp import success $cmdStr")
            return BaseResponse(code = configCmd.value,message = configCmd.message)
        }
        JLog.t(tag,"Frp import failed")
        return BaseResponse(500, message = "Failed")
    }

    private fun frpStart(uid:String):BaseResponse{
        val client = LocalServer.instance.getClient(Config.FRP_PACKAGE_NAME) ?: return BaseResponse(message = "Frp not start")
        client.send(BaseLocalCmd(LocalCmd.FRP_START, data = uid).toString())
        val cmdStr = client.receiveCmd()
        val configCmd = GsonUtil.getGsonObject<BaseLocalCmd<Any?>>(cmdStr)
        configCmd?.let {
            JLog.t(tag,"Frp start success $cmdStr")
            return BaseResponse(code = configCmd.value, message = configCmd.message)
        }
        JLog.t(tag,"Frp start failed")
        return BaseResponse(500, message = "Failed")
    }

    private fun frpStop(uid:String):BaseResponse{
        val client = LocalServer.instance.getClient(Config.FRP_PACKAGE_NAME) ?: return BaseResponse(message = "Frp not start")
        client.send(BaseLocalCmd(LocalCmd.FRP_STOP, data = uid).toString())
        val cmdStr = client.receiveCmd()
        val configCmd = GsonUtil.getGsonObject<BaseLocalCmd<Any?>>(cmdStr)
        configCmd?.let {
            JLog.t(tag,"Frp stop success $cmdStr")
            return BaseResponse(code = configCmd.value,message = configCmd.message)
        }
        JLog.t(tag,"Frp stop failed")
        return BaseResponse(500, message = "Failed")
    }

    private fun frpConfig(uid:String):BaseResponse{
        val client = LocalServer.instance.getClient(Config.FRP_PACKAGE_NAME) ?: return BaseResponse(message = "Frp not start")
        client.send(BaseLocalCmd(LocalCmd.FRP_CONFIG,data = uid).toString())
        val cmdStr = client.receiveCmd()
        val configCmd = GsonUtil.getGsonObject<BaseLocalCmd<FrpConfig>>(cmdStr)
        configCmd?.let {
            JLog.t(tag,"Frp config success $cmdStr")
            return BaseResponse(code = configCmd.value,message = configCmd.message, data = configCmd.data)
        }
        JLog.t(tag,"Frp config failed")
        return BaseResponse(500, message = "Failed")
    }

    private fun frpEdit(configStr:String):BaseResponse{
        val client = LocalServer.instance.getClient(Config.FRP_PACKAGE_NAME) ?: return BaseResponse(message = "Frp not start")
        client.send(BaseLocalCmd(LocalCmd.FRP_EDIT, data = configStr).toString())
        val cmdStr = client.receiveCmd()
        val configCmd = GsonUtil.getGsonObject<BaseLocalCmd<Any?>>(cmdStr)
        configCmd?.let {
            JLog.t(tag,"Frp edit success $cmdStr")
            return BaseResponse(code = configCmd.value,message = configCmd.message)
        }
        JLog.t(tag,"Frp edit failed")
        return BaseResponse(500, message = "Failed")
    }
    private fun login() : BaseResponse {
        JLog.o(tag,"login success")
        return BaseResponse()
    }
    private fun netState() : BaseResponse {
        val netState = NetworkUtils.getNetState(App.app)
        val speedArray = WifiApControl.getInstance().getNetSpeed()
        val numClients = WifiApControl.getInstance().getNumClients()
        val data = NetSpeed(speedArray[0],speedArray[1],netState,numClients)
        val response = BaseResponse(data = data)
        JLog.o(tag,"netState data $data")
        return response
    }
    private fun deviceInfo(): BaseResponse {
        val deviceInfo = DeviceInfo()
        deviceInfo.iccid = WifiApControl.getInstance().getIccid()
        deviceInfo.imei = WifiApControl.getInstance().getImei()
        deviceInfo.linkTime = appRuntime
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
        deviceInfo.dataPlan = dataPlan
        deviceInfo.monthUsed = WifiApControl.getInstance().getMonthUsed()
        deviceInfo.simType = WifiApControl.getInstance().getSimType()
        JLog.o(tag,"deviceInfo data $deviceInfo")
        val response = BaseResponse(data = deviceInfo)
        return response
    }

    /**
     * 获取设备热点属性
     */
    private fun getApConfig():BaseResponse{
        val apConfig = ApConfig()
        val config = runBlocking {
            WifiApControl.getInstance().getSoftApConfig()
        }
        apConfig.softInitData(config)
        JLog.o(tag,"config data: $apConfig")
        return BaseResponse(data = apConfig)
    }
    private fun setApConfig(str: String):BaseResponse{
        JLog.o(tag,"setConfig data: $str")
        val apConfig = Gson().fromJson(str,ApConfig::class.java)
        //应用apConfig数据到SoftApConfig
        val configuration = runBlocking {
            WifiApControl.getInstance().getSoftApConfig()
        }
        runBlocking {
            configuration?.let { configuration ->
                apConfig.data2SoftApConfig(configuration)
                //把修改好的数据修改到系统中
                WifiApControl.getInstance().setApConfig(configuration)
            }
        }
        return BaseResponse()
    }

    private fun analysisFileDownload(map:Map<String,String>, map2:Map<String,String>):Response{
        return getStringResponse("")
    }

    private fun getStringResponse(str: String): Response {
        val newFixedLengthResponse = newFixedLengthResponse(str)
        newFixedLengthResponse.addHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "*")
        newFixedLengthResponse.addHeader(
            HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS,
            "GET, POST, PUT, OPTIONS"
        )
        newFixedLengthResponse.addHeader(
            HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS,
            "Origin, X-Requested-With, Content-Type, Accept, Authorization"
        )
        newFixedLengthResponse.addHeader(
            HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS,
            "Content-Length, Access-Control-Allow-Origin, Access-Control-Allow-Headers, Cache-Control, Content-Language, Content-Type"
        )
        newFixedLengthResponse.addHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS, "true")
        newFixedLengthResponse.addHeader(
            HttpHeaders.CONTENT_TYPE,
            "application/json; charset=utf-8"
        )
        return newFixedLengthResponse
    }

    fun startServer() {
        start(SOCKET_READ_TIMEOUT, false)
        JLog.r(tag,"Start HttpServer")
    }

}