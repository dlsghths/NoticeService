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
import android.os.Parcelable
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.TableLayout
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.os.bundleOf
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.fragment.app.FragmentTransaction
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.gms.tasks.Task
import com.google.android.material.slider.Slider
import com.google.android.material.tabs.TabLayout
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat

class MainActivity : AppCompatActivity() {

    val text_title = "테스트 파일 타이틀"
    val text_content = "테스트 파일 컨텐츠"
    val CHANNL_ID = "Notice_Channel_01"

    val fragment_all_activity = Fragment_All()
    val fragment_select_activity = Fragment_Select()

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

        tablayout.viewTreeObserver.addOnGlobalLayoutListener {
            var a = tablayout.selectedTabPosition
            if (tablayout.selectedTabPosition == 0 && fragment_select_activity.isVisible == true)
            {
                tablayout.getTabAt(1)?.select()
            }
            else if(tablayout.selectedTabPosition == 1 && fragment_all_activity.isVisible == true)
            {
                tablayout.getTabAt(0)?.select()
            }
        }
        // 알람 설정
        // createNotificationChannel()
        // alarm_calling()
    }

    private fun createNotificationChannel() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            val name = text_title
            val descriptionText = text_content
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNL_ID, name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    /*
    60000 = 1분
    3600000 = 1시간
    86400000 = 1일
    2592000000 = 30일
     */
    fun alarm_calling() {
        val intent = Intent(this, AlertDialog::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, intent, 0)

        var builder = NotificationCompat.Builder(this, CHANNL_ID)
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setWhen(System.currentTimeMillis() + 60000)
            .setContentTitle(text_title)
            .setContentText(text_content)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        with(NotificationManagerCompat.from(this))
        {
            notify(0, builder.build())
        }
    }

    fun layout_check() {

    }
}