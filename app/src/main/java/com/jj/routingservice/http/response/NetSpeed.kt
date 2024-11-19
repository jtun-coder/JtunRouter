package com.jj.routingservice.http.response

data class NetSpeed(val totalRxBytes:Long,val totalTxBytes:Long,val state:Int,val numClients:Int)