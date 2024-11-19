package com.jj.routingservice.util

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkRequest
import android.net.wifi.WifiManager
import android.net.wifi.p2p.WifiP2pManager
import android.os.Handler
import android.os.Looper
import androidx.core.content.getSystemService
import timber.log.Timber

object Services {
    private lateinit var contextInit: () -> Context
    val context by lazy { contextInit() }
    fun init(context: () -> Context) {
        contextInit = context
    }

    val mainHandler by lazy { Handler(Looper.getMainLooper()) }
    val connectivity by lazy { context.getSystemService<ConnectivityManager>()!! }
    val p2p by lazy {
        try {
            context.getSystemService<WifiP2pManager>()
        } catch (e: RuntimeException) {
            Timber.w(e)
            null
        }
    }
    val wifi by lazy { context.getSystemService<WifiManager>()!! }

    val netd by lazy @SuppressLint("WrongConstant") { context.getSystemService("netd")!! }

    fun registerNetworkCallback(request: NetworkRequest, networkCallback: ConnectivityManager.NetworkCallback) =
        connectivity.registerNetworkCallback(request, networkCallback, mainHandler)
}
