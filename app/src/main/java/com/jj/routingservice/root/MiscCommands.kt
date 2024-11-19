package com.jj.routingservice.root

import android.content.Context
import android.os.Build
import android.os.Parcelable
import android.os.RemoteException
import android.provider.Settings
import androidx.annotation.RequiresApi
import be.mygod.librootkotlinx.*
import com.jj.routingservice.App.Companion.app
import com.jj.routingservice.net.Routing.Companion.IP
import com.jj.routingservice.net.Routing.Companion.IPTABLES
import com.jj.routingservice.net.TetheringManager
import com.jj.routingservice.net.VpnFirewallManager
import com.jj.routingservice.util.Services
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.onClosed
import kotlinx.coroutines.channels.onFailure
import kotlinx.coroutines.channels.produce
import kotlinx.parcelize.Parcelize
import java.io.File
import java.io.FileOutputStream
import java.io.InterruptedIOException

fun ProcessBuilder.fixPath(redirect: Boolean = false) = apply {
    environment().compute("PATH") { _, value ->
        if (value.isNullOrEmpty()) "/system/bin" else "$value:/system/bin"
    }
    redirectErrorStream(redirect)
}

@Parcelize
data class Dump(val path: String, val cacheDir: File = app.deviceStorage.codeCacheDir) : RootCommandNoResult {
    override suspend fun execute() = withContext(Dispatchers.IO) {
        FileOutputStream(path, true).use { out ->
            val process = ProcessBuilder("sh").fixPath(true).start()
            process.outputStream.bufferedWriter().use { commands ->
                commands.appendLine("""
                    |echo dumpsys ${Context.WIFI_P2P_SERVICE}
                    |dumpsys ${Context.WIFI_P2P_SERVICE}
                    |echo
                    |echo dumpsys ${Context.CONNECTIVITY_SERVICE} tethering
                    |dumpsys ${Context.CONNECTIVITY_SERVICE} tethering
                    |echo
                """.trimMargin())
                if (Build.VERSION.SDK_INT >= 29) {
                    val dumpCommand = if (Build.VERSION.SDK_INT >= 33) {
                        "dumpsys ${Context.CONNECTIVITY_SERVICE} trafficcontroller"
                    } else VpnFirewallManager.DUMP_COMMAND
                    commands.appendLine("echo $dumpCommand\n$dumpCommand\necho")
                    if (Build.VERSION.SDK_INT >= 31) commands.appendLine(
                        "settings get global ${VpnFirewallManager.UIDS_ALLOWED_ON_RESTRICTED_NETWORKS}")
                }
                commands.appendLine("""
                    |echo iptables -t filter
                    |iptables-save -t filter
                    |echo
                    |echo iptables -t nat
                    |iptables-save -t nat
                    |echo
                    |echo ip6tables-save
                    |ip6tables-save
                    |echo
                    |echo ip rule
                    |$IP rule
                    |echo
                    |echo ip neigh
                    |$IP neigh
                    |echo
                    |echo iptables -nvx -L vpnhotspot_fwd
                    |$IPTABLES -nvx -L vpnhotspot_fwd
                    |echo
                    |echo iptables -nvx -L vpnhotspot_acl
                    |$IPTABLES -nvx -L vpnhotspot_acl
                    |echo
                    |echo logcat-su
                    |logcat -d
                """.trimMargin())
            }
            process.inputStream.copyTo(out)
            when (val exit = process.waitFor()) {
                0 -> { }
                else -> out.write("Process exited with $exit".toByteArray())
            }
        }
        null
    }
}

sealed class ProcessData : Parcelable {
    @Parcelize
    data class StdoutLine(val line: String) : ProcessData()
    @Parcelize
    data class StderrLine(val line: String) : ProcessData()
    @Parcelize
    data class Exit(val code: Int) : ProcessData()
}

@Parcelize
class ProcessListener(private val terminateRegex: Regex,
                      private vararg val command: String) : RootCommandChannel<ProcessData> {
    override fun create(scope: CoroutineScope) = scope.produce(Dispatchers.IO, capacity) {
        val process = ProcessBuilder(*command).start()
        val parent = Job()  // we need to destroy process before joining, so we cannot use coroutineScope
        try {
            launch(parent) {
                try {
                    process.inputStream.bufferedReader().useLines { lines ->
                        for (line in lines) {
                            trySend(ProcessData.StdoutLine(line)).onClosed { return@useLines }.onFailure { throw it!! }
                            if (terminateRegex.containsMatchIn(line)) process.destroy()
                        }
                    }
                } catch (_: InterruptedIOException) { }
            }
            launch(parent) {
                try {
                    process.errorStream.bufferedReader().useLines { lines ->
                        for (line in lines) trySend(ProcessData.StdoutLine(line)).onClosed {
                            return@useLines
                        }.onFailure { throw it!! }
                    }
                } catch (_: InterruptedIOException) { }
            }
            launch(parent) {
                trySend(ProcessData.Exit(process.waitFor())).onClosed { return@launch }.onFailure { throw it!! }
            }
            parent.join()
        } finally {
            parent.cancel()
            if (process.isAlive) process.destroyForcibly()
            parent.join()
        }
    }
}

@Parcelize
class ReadArp : RootCommand<ParcelableString> {
    override suspend fun execute() = withContext(Dispatchers.IO) {
        ParcelableString(File("/proc/net/arp").readText())
    }
}

@Parcelize
@RequiresApi(30)
data class StartTethering(private val type: Int,
                          private val showProvisioningUi: Boolean) : RootCommand<ParcelableInt?> {
    override suspend fun execute(): ParcelableInt? {
        val future = CompletableDeferred<Int?>()
        val callback = object : TetheringManager.StartTetheringCallback {
            override fun onTetheringStarted() {
                future.complete(null)
            }

            override fun onTetheringFailed(error: Int?) {
                future.complete(error!!)
            }
        }
        TetheringManager.startTethering(type, true, showProvisioningUi, {
            GlobalScope.launch(Dispatchers.Unconfined) { it.run() }
        }, TetheringManager.proxy(callback))
        return future.await()?.let { ParcelableInt(it) }
    }
}

@Deprecated("Old API since API 30")
@Parcelize
@Suppress("DEPRECATION")
data class StartTetheringLegacy(private val cacheDir: File, private val type: Int,
                                private val showProvisioningUi: Boolean) : RootCommand<ParcelableBoolean> {
    override suspend fun execute(): ParcelableBoolean {
        val future = CompletableDeferred<Boolean>()
        val callback = object : TetheringManager.StartTetheringCallback {
            override fun onTetheringStarted() {
                future.complete(true)
            }

            override fun onTetheringFailed(error: Int?) {
                check(error == null)
                future.complete(false)
            }
        }
        TetheringManager.startTetheringLegacy(type, showProvisioningUi, callback, cacheDir = cacheDir)
        return ParcelableBoolean(future.await())
    }
}

@Parcelize
data class StopTethering(private val type: Int) : RootCommandNoResult {
    override suspend fun execute(): Parcelable? {
        TetheringManager.stopTethering(type)
        return null
    }
}

@Parcelize
data class SettingsGlobalPut(val name: String, val value: String) : RootCommandNoResult {
    companion object {
        suspend fun int(name: String, value: Int) {
            try {
                check(Settings.Global.putInt(Services.context.contentResolver, name, value))
            } catch (e: SecurityException) {
                try {
                    RootManager.use { it.execute(SettingsGlobalPut(name, value.toString())) }
                } catch (eRoot: Exception) {
                    eRoot.addSuppressed(e)
                    throw eRoot
                }
            }
        }
    }

    override suspend fun execute() = withContext(Dispatchers.IO) {
        val process = ProcessBuilder("settings", "put", "global", name, value).fixPath(true).start()
        val error = process.inputStream.bufferedReader().readText()
        val exit = process.waitFor()
        if (exit != 0 || error.isNotEmpty()) throw RemoteException("Process exited with $exit: $error")
        null
    }
}
