package com.jtun.router

import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @OptIn(ExperimentalStdlibApi::class)
    @Test
    fun addition_isCorrect() {
        val ts = 1733797894
        System.out.println("ts" + intToByteArray4(ts).toHexString())
    }

    private fun intToByteArray4(num: Int): ByteArray {
        var byteArray = ByteArray(4)
        var highH = ((num shr 24) and 0xff).toByte()
        var highL = ((num shr 16) and 0xff).toByte()
        var LowH = ((num shr 8) and 0xff).toByte()
        var LowL = (num and 0xff).toByte()
        byteArray[0] = highH
        byteArray[1] = highL
        byteArray[2] = LowH
        byteArray[3] = LowL
        return byteArray
    }

}