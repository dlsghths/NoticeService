package com.healthcare.noticeservice

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import androidx.core.content.ContextCompat
import com.google.android.material.tabs.TabLayout

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 관리가 필요한 Fragment 화면 선언
        val fragment_activity_patient = Fragment_patient()
        val fragment_activity_staff = Fragment_staff()
        val fragment_activity_user = Fragment_user()

        // 로그인을 통해 가져온 정보 정리
        // userName = 로그인 한 사용자의 이름
        val login_user_name = intent.getStringExtra("userName")

        val tablayout = findViewById<TabLayout>(R.id.tab)
        // 화면 접속시 전체 환자용 Fragment를 띄운다
        supportFragmentManager.beginTransaction().replace(R.id.frameLayout, fragment_activity_patient).commit()

        // tablayout에서 버튼이 클릭되었을 경우 이벤트 처리
        // 선택한 Fragment를 MainActivity에 띄운다
        tablayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            // tab의 상태가 선택되지 않음에서 선택 상태로 변경
            override fun onTabSelected(tab: TabLayout.Tab) {
                /*
                0 = 전체 환자용
                1 = 전체 의료용
                2 = 개인 사용자 선택
                */
                var pos = tab.position.toInt()

                if(pos == 0)
                {
                    supportFragmentManager.beginTransaction().replace(R.id.frameLayout, fragment_activity_patient).commit()
                }
                else if(pos == 1)
                {
                    supportFragmentManager.beginTransaction().replace(R.id.frameLayout, fragment_activity_staff).commit()
                }
                else
                {
                    // 사용자가 사용하는 이름에 대한 정보를 bundle 전달
                    val bundle = Bundle()
                    bundle.putString("userName", login_user_name)
                    fragment_activity_patient.arguments = bundle
                    supportFragmentManager.beginTransaction().replace(R.id.frameLayout, fragment_activity_user).commit()
                }
            }
            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }
            override fun onTabReselected(tab: TabLayout.Tab?) {

            }
        })
    }
}