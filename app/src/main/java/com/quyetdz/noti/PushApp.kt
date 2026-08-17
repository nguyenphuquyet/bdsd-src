package com.quyetdz.noti

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build

class PushApp : Application() {

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager = getSystemService(NotificationManager::class.java)

            notificationManager.deleteNotificationChannel("push_app_channel")

            val channel = NotificationChannel(
                "push_app_channel_v2",
                "Thông báo chung",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                enableLights(true)
                enableVibration(true)
            }
            notificationManager.createNotificationChannel(channel)
        }
    }
}