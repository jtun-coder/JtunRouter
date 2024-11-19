package com.jj.routingservice.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ClientConnected(@PrimaryKey
                           val mac: String,//mac
                           var nickname: String = "",//名称
                           var ipAddress : String = "",//ip地址
                           var linkTime:Long = 0,//连接时间
                           var allowInternet:Boolean = true, //是否允许连接网络
                           var onLine : Boolean = false){ //是否在线
}
