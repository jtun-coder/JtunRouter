package com.jj.routingservice.util

import android.content.Intent
import com.jj.routingservice.App
import com.jj.routingservice.config.ServerConfig
import com.jj.routingservice.frp.FrpcService
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