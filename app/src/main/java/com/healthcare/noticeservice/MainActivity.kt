package com.healthcare.noticeservice

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import com.google.android.material.tabs.TabLayout

class MainActivity : AppCompatActivity() {
    // 관리가 필요한 Fragment 화면 선언
    val activityPatient = Fragment_patient()
    val activityStaff = Fragment_staff()
    val activityUser = Fragment_user()

    // false = item.text "선택"
    // true = item.text "저장"
    var optionCheck: Boolean = false
    lateinit var menuTake : Menu

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 로그인을 통해 가져온 정보 정리
        // userName = 로그인 한 사용자의 이름
        val sharedPref = getSharedPreferences("UserName", MODE_PRIVATE)
        val loginUserName = sharedPref.getString("User_id", "")

        val tablayout = findViewById<TabLayout>(R.id.tab)
        // 화면 접속시 전체 환자용 Fragment를 띄운다
        supportFragmentManager.beginTransaction().replace(R.id.frameLayout, activityUser)
            .commit()
        supportActionBar?.setTitle("개인용")

        // tablayout에서 버튼이 클릭되었을 경우 이벤트 처리
        // 선택한 Fragment를 MainActivity에 띄운다
        tablayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
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
}