package com.healthcare.noticeservice

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.core.app.NotificationCompat

class MyReceiver : BroadcastReceiver() {

    lateinit var notificationManager: NotificationManager

    override fun onReceive(context: Context, intent: Intent) {
        notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        createNotificationChannel()
        deliverNotification(context)
    }

    fun createNotificationChannel() {
        // 안드로이드 오레오 버전 이상부터는 Notification을 사용하기 위해서 채널을 등록하여야 한다.
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                    Constants.CHANNEL_ID,
                    "채널의 이름",
                    NotificationManager.IMPORTANCE_HIGH
            )
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.RED
            notificationChannel.enableVibration(true)
            notificationChannel.description = "채널의 상세정보입니다."
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }

    fun deliverNotification(context: Context) {
        val contentIntent = Intent(context, MainActivity::class.java)
        val contentPendingIntent = PendingIntent.getActivity(
                context,
                Constants.Notification_ID,
                contentIntent,
                PendingIntent.FLAG_CANCEL_CURRENT
        )

        val builder = NotificationCompat.Builder(context, Constants.CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle("타이틀 화면")
                .setContentText("내용 화면")
                .setContentIntent(contentPendingIntent)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setWhen(System.currentTimeMillis())
                .setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL)

        notificationManager.notify(Constants.Notification_ID, builder.build())
    }
}