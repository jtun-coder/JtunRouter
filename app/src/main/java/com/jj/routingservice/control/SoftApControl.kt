package com.jj.routingservice.control

import android.net.LinkAddress
import android.net.MacAddress
import android.os.Build
import android.os.Parcelable
import android.system.OsConstants
import android.text.TextUtils
import androidx.annotation.RequiresApi
import androidx.lifecycle.lifecycleScope
import com.jj.routingservice.net.IpNeighbour
import com.jj.routingservice.net.monitor.IpNeighbourMonitor
import com.jj.routingservice.net.wifi.WifiApManager
import com.jj.routingservice.net.wifi.WifiClient
import com.jj.routingservice.room.AppDatabase
import com.jj.routingservice.room.ClientConnected
import com.jj.routingservice.root.RootManager
import com.jj.routingservice.root.TetheringCommands
import com.jj.routingservice.root.WifiApCommands
import com.jj.routingservice.util.KLog
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.launch
import timber.log.Timber
import java.net.Inet4Address
import kotlin.coroutines.cancellation.CancellationException

/**
 * Ap状态管理
 */
class SoftApControl:WifiApManager.SoftApCallbackCompat, IpNeighbourMonitor.Callback{
    private val clazz by lazy { Class.forName("android.net.wifi.SoftApCapability") }
    private val getMaxSupportedClients by lazy { clazz.getDeclaredMethod("getMaxSupportedClients") }
    private val clients: MutableList<Parcelable> = mutableListOf()
    companion object {
        private val classTetheredClient by lazy { Class.forName("android.net.TetheredClient") }
        private val getMacAddress by lazy { classTetheredClient.getDeclaredMethod("getMacAddress") }
        private val getAddresses by lazy { classTetheredClient.getDeclaredMethod("getAddresses") }
        private val getTetheringType by lazy { classTetheredClient.getDeclaredMethod("getTetheringType") }

        private val classAddressInfo by lazy { Class.forName("android.net.TetheredClient\$AddressInfo") }
        private val getAddress by lazy { classAddressInfo.getDeclaredMethod("getAddress") }
        private val getHostname by lazy { classAddressInfo.getDeclaredMethod("getHostname") }
    }
    fun start(){
        WifiApCommands.registerSoftApCallback(this)
        IpNeighbourMonitor.registerCallback(this,true)
        if (Build.VERSION.SDK_INT >= 30){
            GlobalScope.launch {
                try {
                    RootManager.use {
                        handleClientsChanged(it.create(TetheringCommands.RegisterTetheringEventCallback(), this))
                    }
                } catch (_: CancellationException) {
                } catch (e: Exception) {
                    Timber.w(e)
                }
            }

        }
    }
    private suspend fun handleClientsChanged(
        channel: ReceiveChannel<TetheringCommands.OnClientsChanged>,
    ) = channel.consumeEach { event ->
        KLog.i("OnClientsChanged clients : ${event.clients}")
        event.clients.map {client ->
            val macAddress = getMacAddress(client) as MacAddress
            val clientConnected = AppDatabase.instance.clientDao.lookupOrDefault(macAddress.toString())
            val addresses = getAddresses(client) as List<*>
            if(addresses.isNotEmpty()){
                val address = getAddress(addresses[0]) as LinkAddress
                val hostName = getHostname(addresses[0])
                if(TextUtils.isEmpty(clientConnected.ipAddress)){//Ip为空代表为第一次连接
                    if (hostName != null) {
                        clientConnected.nickname = hostName.toString()
                        clientConnected.linkTime = System.currentTimeMillis()
                    }
                }
                clientConnected.ipAddress = address.address.hostName
            }
            //更新连接时间，判断上一次存储的数组是否有该设备，如果没有更新连接时间
            if(!checkConClient(macAddress.toString())){//没包含更新linkTime
                clientConnected.linkTime = System.currentTimeMillis()
            }
            AppDatabase.instance.clientDao.update(clientConnected)
        }
        clients.clear()
        clients.addAll(event.clients)
    }

    /**
     * 是否包含该设备
     */
    private fun checkConClient(macAddress: String) : Boolean{
        for (client in clients){
            val mac = getMacAddress(client) as MacAddress
            if(macAddress == mac.toString()){
                return true
            }
        }
        return false
    }

    /**
     * 获取已连接过的客户端列表
     */
    suspend fun getConnectedClients(): List<ClientConnected> {
        val connectedClients = AppDatabase.instance.clientDao.getList()
        for (client in connectedClients){
            client.onLine = checkConClient(client.mac)
        }
        return connectedClients
    }

    fun stop(){
        WifiApCommands.unregisterSoftApCallback(this)
        IpNeighbourMonitor.unregisterCallback(this)
    }
    override fun onStateChanged(state: Int, failureReason: Int) {
        KLog.i("state： $state ,failureReason : $failureReason")
    }

    /**
     * 当前链接数量
     */
    override fun onNumClientsChanged(numClients: Int) {
        KLog.i("numClients： $numClients ")
    }
    override fun onInfoChanged(info: List<Parcelable>) {
        KLog.i("info： $info ")
    }
    override fun onCapabilityChanged(capability: Parcelable) {
        KLog.i("capability： $capability max : ${getMaxSupportedClients(capability)}")
    }

    /**
     * 设备连接变化
     */
    @RequiresApi(Build.VERSION_CODES.R)
    override fun onConnectedClientsChanged(clients: List<Parcelable>) {
        val wifiAp = clients.map {
            val client = WifiClient(it)
            KLog.i("onConnected Client : $client")
//            val tetheringType = getTetheringType(it)
            val addresses = getMacAddress(it)
            KLog.i("onConnectedClientsChanged getMacAddress : $addresses")

        }
    }
    fun getNumClients():Int{
        return clients.size
    }
    /**
     * 设备连接信息
     */
    override fun onIpNeighbourAvailable(neighbours: Collection<IpNeighbour>) {
        neighbours.map {
            //添加设备到数据库
            if(it.ip is Inet4Address && it.state == IpNeighbour.State.VALID){

            }
        }
        KLog.i("onIpNeighbourAvailable $neighbours")
    }
}