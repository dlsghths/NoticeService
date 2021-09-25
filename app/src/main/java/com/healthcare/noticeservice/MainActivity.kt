package com.healthcare.noticeservice

import android.app.AlertDialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.android.material.tabs.TabLayout

class MainActivity : AppCompatActivity() {

    val fragment_all_activity = Fragment_patient()
    val fragment_select_activity = Fragment_user()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /*
        로그인을 통해 가져온 정보 정리
        userName = 로그인 한 사용자의 이름
        */
        val login_user_name = intent.getStringExtra("userName")

        val tablayout = findViewById<TabLayout>(R.id.tab)
        val bundle = Bundle()
        bundle.putString("userName", login_user_name)
        fragment_all_activity.arguments = bundle
        supportFragmentManager.beginTransaction().replace(R.id.frameLayout, fragment_all_activity).commit()

        tablayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            // tab의 상태가 선택되지 않음에서 선택 상태로 변경
            override fun onTabSelected(tab: TabLayout.Tab) {
                var pos = tab.position.toInt()

                // 전체 버튼일 경우
                if(pos == 0)
                {
                    fragment_all_activity.arguments = bundle
                    supportFragmentManager.beginTransaction().replace(R.id.frameLayout, fragment_all_activity).commit()
                }
                // 일부 버튼일 경우
                else
                {
                    fragment_select_activity.arguments = bundle
                    supportFragmentManager.beginTransaction().replace(R.id.frameLayout, fragment_select_activity).commit()
                }
            }

            // tab의 상태가 선택 상태에서 선택되지 않음으로 변경
            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }
            // 이미 선택된 상태의 tab이 사용자에 의해 다시 선택됨
            override fun onTabReselected(tab: TabLayout.Tab?) {

            }
        })
    }
}