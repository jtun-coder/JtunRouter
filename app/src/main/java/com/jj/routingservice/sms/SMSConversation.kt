package com.jj.routingservice.sms

data class SMSConversation(val address:String,//发送人地址
                           val lastMessage:Sms, //最后一条信息
                           val threadId:Int)//会话id
