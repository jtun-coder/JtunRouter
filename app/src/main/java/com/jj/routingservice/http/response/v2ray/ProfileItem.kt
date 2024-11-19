package com.jj.routingservice.http.response.v2ray

data class ProfileItem(
    val configType: EConfigType,
    var subscriptionId: String = "",
    var remarks: String = "",
    var server: String?,
    var serverPort: Int?,
)