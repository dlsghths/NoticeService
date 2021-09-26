package com.healthcare.noticeservice

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import android.os.SystemClock
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class AlarmSetting : AppCompatActivity() {
    // Fragment_patient에서 개인 목록 추가
    // Fragment_staff에서 개인 목록 추가
    // Fragment_user에서 개인 목록 삭제

    // Notification_ID, hospital_name
    fun alarm_setting() {
        val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager

        // notification_ID 만들기
        val alarTime = SystemClock.elapsedRealtime()



        val intent = Intent(this, MyReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(this, Constants.Notification_ID,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        /*
        val triggerTime = SystemClock.elapsedRealtime() + Constants.alarm_time * 1000
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.ELAPSED_REALTIME_WAKEUP,
                triggerTime,
                pendingIntent
            )
        }
         */

        val repeatInterval : Long =
    }
}