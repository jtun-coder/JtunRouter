package com.jj.routingservice.http.response.v2ray

data class Configs(val selected:String?,val isRunning:Boolean, val list: List<ServersCache>)
