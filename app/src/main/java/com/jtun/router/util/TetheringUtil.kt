package com.jtun.router.util

import android.content.Intent
import com.jtun.router.App
import com.jtun.router.TetheringService
import com.jtun.router.control.WifiApControl

object TetheringUtil {
    fun startTetheringService(){
        App.app.startForegroundService(
            Intent(App.app, TetheringService::class.java)
            .putExtra(TetheringService.EXTRA_ADD_INTERFACES, arrayOf(WifiApControl.getInstance().getActiveIFace())))
    }
    fun stopTetheringService(){
        App.app.stopService(
            Intent(App.app, TetheringService::class.java)
                .putExtra(TetheringService.EXTRA_ADD_INTERFACES, arrayOf(WifiApControl.getInstance().getActiveIFace())))
    }
}