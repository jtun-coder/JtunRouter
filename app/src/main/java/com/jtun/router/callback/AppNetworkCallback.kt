package com.jtun.router.callback

import android.net.ConnectivityManager
import android.net.LinkProperties
import android.net.Network
import android.net.NetworkCapabilities
import com.jtun.router.App
import com.jtun.router.util.FrpUtil
import com.jtun.router.util.KLog
import com.jtun.router.util.NetworkUtils
import com.jtun.router.util.TetheringUtil

class AppNetworkCallback : ConnectivityManager.NetworkCallback() {
    //当网络状态修改但仍旧是可用状态时调用
    override fun onCapabilitiesChanged(network: Network, networkCapabilities: NetworkCapabilities) {
        super.onCapabilitiesChanged(network, networkCapabilities)
        if (NetworkUtils.isConnectedAvailableNetwork(App.app)) {
            KLog.d("onCapabilitiesChanged ---> ====网络可正常上网===网络类型为： " + NetworkUtils.getConnectedNetworkType(App.app))
            TetheringUtil.startTetheringService()
            FrpUtil.startFrp()
        }

        //表明此网络连接验证成功
        if (networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)) {
            if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                KLog.d("===当前在使用Mobile流量上网===")
            } else if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                KLog.d("====当前在使用WiFi上网===")
            } else if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH)) {
                KLog.d("=====当前使用蓝牙上网=====")
            } else if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                KLog.d("=====当前使用以太网上网=====")
            } else if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_VPN)) {
                KLog.d("===当前使用VPN上网====")
            } else if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI_AWARE)) {
                KLog.d("===表示此网络使用Wi-Fi感知传输====")
            } else if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_LOWPAN)) {
                KLog.d("=====表示此网络使用LoWPAN传输=====")
            } else if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_USB)) {
                KLog.d("=====表示此网络使用USB传输=====")
            }
        }
    }

    override fun onAvailable(network: Network) {
        super.onAvailable(network)
        KLog.e("==网络连接成功，通知可以使用的时候调用====onAvailable===")
    }

    override fun onUnavailable() {
        KLog.e("==当网络连接超时或网络请求达不到可用要求时调用====onUnavailable===")
        super.onUnavailable()
    }

    override fun onBlockedStatusChanged(network: Network, blocked: Boolean) {
        KLog.e("==当访问指定的网络被阻止或解除阻塞时调用===onBlockedStatusChanged==")
        super.onBlockedStatusChanged(network, blocked)
    }

    override fun onLosing(network: Network, maxMsToLive: Int) {
        KLog.e("==当网络正在断开连接时调用===onLosing===")
        super.onLosing(network, maxMsToLive)
    }

    override fun onLost(network: Network) {
        KLog.e("==当网络已断开连接时调用===onLost===")
        super.onLost(network)
    }

    override fun onLinkPropertiesChanged(network: Network, linkProperties: LinkProperties) {
        KLog.e("==当网络连接的属性被修改时调用===onLinkPropertiesChanged===")
        super.onLinkPropertiesChanged(network, linkProperties)
    }
}