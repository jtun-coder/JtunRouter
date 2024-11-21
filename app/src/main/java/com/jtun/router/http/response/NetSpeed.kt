package com.jtun.router.http.response

data class NetSpeed(val totalRxBytes:Long,val totalTxBytes:Long,val state:Int,val numClients:Int)