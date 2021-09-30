package com.healthcare.noticeservice

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import com.google.android.material.tabs.TabLayout
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList
import kotlin.time.milliseconds

class MainActivity : AppCompatActivity() {
    // 관리가 필요한 Fragment 화면 선언
    val activityPatient = Fragment_patient()
    val activityStaff = Fragment_staff()
    val activityUser = Fragment_user()

    // false = item.text "선택"
    // true = item.text "저장"
    var optionCheck: Boolean = false
    lateinit var menuTake: Menu

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val firebaseDatabase = Firebase.database.getReference()

        // 저장되어 있는 sharedPreferences 사용자 정보 확인
        val prefUserName = getSharedPreferences("UserName", MODE_PRIVATE)
        val loginUserName = prefUserName.getString("User_id", "").toString()
        // 저장되어 있는 sharedPreferences 병원 정보 확인
        val prefHospital = getSharedPreferences("hospital", MODE_PRIVATE)
        if (prefHospital.all.isEmpty()) {
            // 사용자가 내부에 가지고 있는 정보가 없을 경우
            // firebase에는 사용자가 선택한 병원 리스트가 있는지 확인
            firebaseDatabase.get().addOnSuccessListener {
                val userInfo = it.child("사용자").child(loginUserName).value.toString()
                if (userInfo == "없음") {
                    // 사용자가 특정한 데이터 정보가 없을 경우
                    // finish
                } else {
                    // 사용자가 특정한 데이터 정보가 있을 경우
                    // 해당 사용자가 선택한 병원 리스트를 split으로 나누어 저장
                    val userInfoSplit = userInfo.split(", ")
                    // 나눈 병원 리스트 넘버를 확인해서 병원 리스트에 맞는 데이터 값 받아오기
                    var count: Int = 0
                    val sharedPref = getSharedPreferences("hospital", MODE_PRIVATE)
                    val sharedPrefEditor = sharedPref.edit()
                    // sharedPreferences에 해당 리스트를 저장
                    for (info in userInfoSplit) {
                        sharedPrefEditor.putString(userInfoSplit[count], it.child("병원").child(userInfoSplit[count]).value.toString())
                        sharedPrefEditor.commit()
                        count++
                    }
                    // sharedPreferences를 확인해서 알림 목록을 등록
                    sharedHospital()
                }
            }
        } else {
            // 사용자가 가지고 있는 정보가 있을 경우
            Log.d("tag", "정보 있음")
        }

        val tablayout = findViewById<TabLayout>(R.id.tab)
        // 화면 접속시 전체 환자용 Fragment를 띄운다
        supportFragmentManager.beginTransaction().replace(R.id.frameLayout, activityUser)
            .commit()
        supportActionBar?.setTitle("개인용")

        sharedHospital()

        // tablayout에서 버튼이 클릭되었을 경우 이벤트 처리
        // 선택한 Fragment를 MainActivity에 띄운다
        tablayout.addOnTabSelectedListener(
            object : TabLayout.OnTabSelectedListener {
                // tab의 상태가 선택되지 않음에서 선택 상태로 변경
                override fun onTabSelected(tab: TabLayout.Tab) {
                    // 저장을 완료해서 item의 텍스트가 "선택"일 경우
                    /*
                    0 = 전체 환자용
                    1 = 전체 의료용
                    2 = 개인 사용자 선택
                    */
                    menuTake.findItem(R.id.actionCheckBox).title = "선택"
                    when (tab.position) {
                        0 -> {
                            supportActionBar?.setTitle("개인용")
                            // 사용자가 사용하는 이름에 대한 정보를 bundle 전달
                            val bundle = Bundle()
                            bundle.putString("userName", loginUserName)
                            activityPatient.arguments = bundle
                            supportFragmentManager.beginTransaction()
                                .replace(R.id.frameLayout, activityUser).commit()
                        }
                        1 -> {
                            supportActionBar?.setTitle("환자용")
                            supportFragmentManager.beginTransaction()
                                .replace(R.id.frameLayout, activityPatient).commit()
                        }
                        2 -> {
                            supportActionBar?.setTitle("의료진용")
                            supportFragmentManager.beginTransaction()
                                .replace(R.id.frameLayout, activityStaff).commit()
                        }
                    }
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {

                }

                override fun onTabReselected(tab: TabLayout.Tab?) {

                }
            })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.actionbar_actions, menu)
        if (menu != null) {
            this.menuTake = menu
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.actionCheckBox) {
            if (item.title == "선택") {
                // 저장으로 item 텍스트를 변경
                item.title = "저장"
                optionCheck = true
            } else {
                // 선택으로 item 텍스트를 변경
                item.title = "선택"
                optionCheck = false
            }

            // 어떤 Fragment 화면이 띄어져 있는지에 따라 호출
            when {
                activityPatient.isVisible -> {
                    activityPatient.optionsItemSelected(item)
                }
                activityStaff.isVisible -> {
                    activityStaff.optionsItemSelected(item)
                }
                activityUser.isVisible -> {
                    activityUser.optionsItemSelected(item)
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    // sharedPreferences에 저장되어 있는 날짜 데이터로 알림 생성
    fun makeAlarm(Notification_ID: Int, Notification_Date: String, Notification_Name: String) {
        val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager

        val intent = Intent(this, MyReceiver::class.java)
        intent.putExtra("Notification_ID", Notification_ID)
        intent.putExtra("Hospital_Name", Notification_Name)

        val pendingIntent = PendingIntent.getBroadcast(
            this,
            Notification_ID,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        val time = SimpleDateFormat("yyyy-MM-dd")
        val stringTime = Notification_Date
        val date = time.parse(stringTime).time

        alarmManager.set(AlarmManager.RTC_WAKEUP, date + 54000000 + 2940000, pendingIntent)
    }

    fun sharedHospital() {
        // 메인화면이 시작되었을 때 가지고 있는 알림 설정 값을 확인한다.
        val sharedPref = getSharedPreferences("hospital", MODE_PRIVATE)

        val keys = sharedPref.all
        for (entry in keys) {
            val split = entry.value.toString().split(", ")
            makeAlarm(entry.key.toInt(), split[1], split[0])
        }
        /*
        var count = 0
        for (info in 0..sharedPref.all.size) {
            val sharedPref_userName = sharedPref.all.entries
            val split = sharedPref_userName.split(", ")

            count++
        }*/
    }
}