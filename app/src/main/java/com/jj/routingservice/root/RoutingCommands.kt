package com.jj.routingservice.root

import android.os.Parcelable
import be.mygod.librootkotlinx.RootCommand
import be.mygod.librootkotlinx.RootCommandNoResult
import com.jj.routingservice.net.Routing
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import kotlinx.parcelize.Parcelize
import timber.log.Timber

object RoutingCommands {

    @Parcelize
    class StopApp(val packageName:String) : RootCommandNoResult {
        override suspend fun execute() = withContext(Dispatchers.IO) {
            val process = ProcessBuilder("sh").fixPath(true).start()
            process.outputStream.bufferedWriter().appendLine("am force-stop $packageName")
            when (val code = process.waitFor()) {
                0 -> { }
                else -> Timber.w("Unexpected exit code $code")
            }
            check(process.waitFor() == 0)
            null
        }
    }
    @Parcelize
    class Clean : RootCommandNoResult {
        override suspend fun execute() = withContext(Dispatchers.IO) {
            val process = ProcessBuilder("sh").fixPath(true).start()
            process.outputStream.bufferedWriter().use(Routing.Companion::appendCleanCommands)
            when (val code = process.waitFor()) {
                0 -> { }
                else -> Timber.w("Unexpected exit code $code")
            }
            check(process.waitFor() == 0)
            null
        }
    }

    class UnexpectedOutputException(msg: String, val result: ProcessResult) : RuntimeException(msg)

    @Parcelize
    data class ProcessResult(val exit: Int, val out: String, val err: String) : Parcelable {
        fun message(command: List<String>, out: Boolean = this.out.isNotEmpty(),
                    err: Boolean = this.err.isNotEmpty()): String? {
            val msg = StringBuilder("${command.joinToString(" ")} exited with $exit")
            if (out) msg.append("\n${this.out}")
            if (err) msg.append("\n=== stderr ===\n${this.err}")
            return if (exit != 0 || out || err) msg.toString() else null
        }

        fun check(command: List<String>, out: Boolean = this.out.isNotEmpty(),
                  err: Boolean = this.err.isNotEmpty()) = message(command, out, err)?.let { msg ->
            throw UnexpectedOutputException(msg, this)
        }
    }

    @Parcelize
    data class Process(val command: List<String>, private val redirect: Boolean = false) : RootCommand<ProcessResult> {
        @Suppress("BlockingMethodInNonBlockingContext")
        override suspend fun execute() = withContext(Dispatchers.IO) {
            val process = ProcessBuilder(command).fixPath(redirect).start()
            coroutineScope {
                val output = async { process.inputStream.bufferedReader().readText() }
                val error = async { if (redirect) "" else process.errorStream.bufferedReader().readText() }
                ProcessResult(process.waitFor(), output.await(), error.await())
            }
        }
    }
}
