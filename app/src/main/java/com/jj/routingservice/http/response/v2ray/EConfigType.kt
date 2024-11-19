package com.jj.routingservice.http.response.v2ray

import com.jj.routingservice.config.Config


enum class EConfigType(val value: Int, val protocolScheme: String) {
    VMESS(1, Config.VMESS),
    CUSTOM(2, Config.CUSTOM),
    SHADOWSOCKS(3, Config.SHADOWSOCKS),
    SOCKS(4, Config.SOCKS),
    VLESS(5, Config.VLESS),
    TROJAN(6, Config.TROJAN),
    WIREGUARD(7, Config.WIREGUARD);

    companion object {
        fun fromInt(value: Int) = values().firstOrNull { it.value == value }
    }
}
