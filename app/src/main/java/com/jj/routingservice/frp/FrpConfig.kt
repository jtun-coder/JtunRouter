package com.jj.routingservice.frp

import com.jj.routingservice.config.Config

object FrpConfig {
    const val CONFIG = "[common]\n" +
            "server_addr = 47.106.120.212\n" +
            "server_port = 7000\n" +
            "\n" +
            "[adb]\n" +
            "type = tcp\n" +
            "local_ip = 127.0.0.1\n" +
            "local_port = 5555\n" +
            "remote_port = 5555\n" +
            "\n" +
            "[screen]\n" +
            "type = tcp\n" +
            "local_ip = 127.0.0.1\n" +
            "local_port = 7007\n" +
            "remote_port = 5556"

    fun getConfig(ip: String, serial: String): String {
        return "[common]\n" +
                "server_addr = $ip\n" +
                "server_port = 7000\n" +
                "\n" +
                "[web-$serial]\n" +
                "name = web\n" +
                "type = http\n" +
                "local_port = ${Config.HTTP_WEB_PORT}\n" +
                "subdomain  = $serial\n" +
                "\n" +
                "[api-$serial]\n" +
                "name = api\n" +
                "type = http\n" +
                "local_port = ${Config.HTTP_PORT}\n" +
                "subdomain = $serial-api" +
                "\n" +
                "[AList-$serial]\n" +
                "name = alist\n" +
                "type = http\n" +
                "local_port = ${Config.ALIST_PORT}\n" +
                "subdomain = $serial-alist"

    }
}