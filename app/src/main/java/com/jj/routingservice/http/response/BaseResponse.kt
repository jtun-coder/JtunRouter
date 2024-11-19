package com.jj.routingservice.http.response

import com.google.gson.Gson

data class BaseResponse(val code :Int = 0,
                        val message:String = "success",
                        val data : Any? = Any()){

    fun toJsonString():String{
        return Gson().toJson(this)
    }
}
