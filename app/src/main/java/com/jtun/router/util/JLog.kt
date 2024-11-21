package com.jtun.router.util

import com.jtun.router.App
import com.jtun.router.room.AppDatabase
import com.jtun.router.room.Log
import kotlinx.coroutines.launch

/**
 * 保存日志到数据库
 */
object JLog {
    const val R = 1 //运行日志
    const val O = 2 //操作日志
    const val T = 3 //第三方应用日志
    const val U = 4 //升级日志
    const val E = 5 //错误日志

    fun log(type:Int,tag:String,msg:String){
        val log = Log(type,tag,msg,System.currentTimeMillis())
        App.app.applicationScope.launch {
            AppDatabase.instance.logDao.update(log)
        }
    }
    fun r(tag:String,msg:String){
        log(R,tag,msg)
    }
    fun o(tag:String,msg:String){
        log(O,tag,msg)
    }
    fun t(tag:String,msg:String){
        log(T,tag,msg)
    }
    fun u(tag:String,msg:String){
        log(U,tag,msg)
    }
    fun e(tag:String,msg:String){
        log(E,tag,msg)
    }
}