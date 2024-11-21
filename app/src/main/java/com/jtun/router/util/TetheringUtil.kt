package com.jtun.router.util

import android.content.Intent
import com.jtun.router.App
import com.jtun.router.TetheringService

object TetheringUtil {
    fun startTetheringService(){
        App.app.startForegroundService(
            Intent(App.app, TetheringService::class.java)
            .putExtra(TetheringService.EXTRA_ADD_INTERFACES, arrayOf("wlan0")))
    }
    fun stopTetheringService(){
        App.app.stopService(
            Intent(App.app, TetheringService::class.java)
                .putExtra(TetheringService.EXTRA_ADD_INTERFACES, arrayOf("wlan0")))
    }
}