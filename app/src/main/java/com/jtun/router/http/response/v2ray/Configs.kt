package com.jtun.router.http.response.v2ray

data class Configs(val selected:String?,val isRunning:Boolean, val list: List<ServersCache>)
