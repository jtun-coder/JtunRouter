package com.jtun.router.root

object Jni {
    init {
        System.loadLibrary("vpnhotspot")
    }
    external fun removeUidInterfaceRules(path: String?, uid: Int, rules: Long): Boolean
}
