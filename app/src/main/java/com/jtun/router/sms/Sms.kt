package com.jtun.router.sms

data class Sms(val id:Int,
               val address:String,//发件人地址
               val person:String?,//如果发件人在通讯录中则为具体姓名，陌生人为null
               val body :String?,//短信具体内容
               val data:String,//时间戳
               val type:Int, //短信类型1是接收到的，2是已发出
               val read:Int = 1)//1表示已读 0为未读
