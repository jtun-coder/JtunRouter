package com.jtun.router.frp

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.graphics.Color
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.jtun.router.R
import com.jtun.router.util.JLog
import com.jtun.router.util.KLog
import frpclib.Frpclib
import org.json.JSONObject
import java.util.UUID

class FrpcService :Service(){
    private val NOTIFY_ID = 0x1010
    private var notificationManager: NotificationManager? = null
    private val uid:String = UUID.randomUUID().toString()//frp启动id
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }


    override fun onCreate() {
        super.onCreate()
        KLog.i()
        notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        startForeground(NOTIFY_ID, createForegroundNotification())
    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let {
            Frpclib.close(uid)
            val frpConfig = intent.getStringExtra("frp_config")
            val jsonObject = JSONObject(frpConfig)
            val frpIp: String = jsonObject.getString("ip")
            val serial: String = jsonObject.getString("serial")
//            val frpPort: Int = 7001
            //启动frp
            Thread{
                JLog.r("Frpc","start frpclib $frpConfig")
                val result = Frpclib.runContent(uid, FrpConfig.getConfig(frpIp,serial))
                KLog.i("runContent result : $result")
            }.start()
        }
        return START_REDELIVER_INTENT
    }

    private fun createForegroundNotification(): Notification {
        val notificationChannelId = "frpc_android_channel"
        val channelName = "Frpc Service Notification"
        val importance = NotificationManager.IMPORTANCE_HIGH
        val notificationChannel =
            NotificationChannel(notificationChannelId, channelName, importance)
        notificationChannel.description = "Frpc Foreground Service"
        notificationChannel.enableLights(true)
        notificationChannel.lightColor = Color.GREEN
        notificationChannel.setVibrationPattern(longArrayOf(0, 1000, 500, 1000))
        notificationChannel.enableVibration(true)
        if (notificationManager != null) {
            notificationManager!!.createNotificationChannel(notificationChannel)
        }
        val builder = NotificationCompat.Builder(this, notificationChannelId)
        builder.setSmallIcon(R.mipmap.ic_launcher)
        builder.setContentTitle("Frpc Foreground Service")
        builder.setContentText("Frpc Service is running")
        builder.setWhen(System.currentTimeMillis())
//        val activityIntent = Intent(this, MainActivity::class.java)
//        val pendingIntent: PendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            PendingIntent.getActivity(
//                this,
//                0,
//                activityIntent,
//                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
//            )
//        } else {
//            PendingIntent.getActivity(this, 0, activityIntent, PendingIntent.FLAG_UPDATE_CURRENT)
//        }
//        builder.setContentIntent(pendingIntent)
        return builder.build()
    }

    override fun onDestroy() {
        super.onDestroy()
        KLog.i()
        stopForeground(STOP_FOREGROUND_REMOVE)
        Frpclib.close(uid)
    }
}