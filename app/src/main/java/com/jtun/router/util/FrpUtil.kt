package com.jtun.router.util

import android.content.Intent
import com.jtun.router.App
import com.jtun.router.config.ServerConfig
import com.jtun.router.frp.FrpcService
import org.json.JSONObject

object FrpUtil {
    fun startFrp(){
        val intent = Intent(App.app, FrpcService::class.java)
        val jsonObject = JSONObject()
        jsonObject.put("ip",ServerConfig.FRP_ADDRESS)
        jsonObject.put("serial",DeviceUtil.getAndroidId(App.app))
        intent.putExtra("frp_config", jsonObject.toString())
        App.app.startService(intent)
    }
    fun stopFrp(){
        val intent = Intent(App.app, FrpcService::class.java)
        App.app.stopService(intent)
    }

}