package com.healthcare.noticeservice

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val editText_login = findViewById<EditText>(R.id.editText_login)
        val button_login = findViewById<Button>(R.id.button_login)
        val database = Firebase.database

        // 자동 로그인 기능
        loginAutoCheck()

        // 로그인 버튼
        button_login.setOnClickListener {
            // 사용자 정보가 있는지 db에 확인 요청
            val editText_login_userId = editText_login.text.toString()

            val databaseRef = database.getReference()

            databaseRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val user = snapshot.child("사용자").child(editText_login_userId).getValue()

                    // 사용자 정보가 있을 경우
                    if (user != null) {
                        // 사용자 아이디 저장
                        sharedLoginUpdate(editText_login_userId)

                        // MainActivity 호출
                        val intent = Intent(this@LoginActivity, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                    // 사용자 정보가 없을 경우
                    else {
                        // 잘못된 계정 정보에 대한 내용 팝업
                        val alert_builder = AlertDialog.Builder(this@LoginActivity)
                            .setTitle("로그인 실패")
                            .setMessage("해당 사용자에 대한 로그인 권한이 없습니다.")
                            .setNegativeButton("확인", null)
                            .create()
                        alert_builder.show()
                    }
                }

                override fun onCancelled(error: DatabaseError) {

                }
            })
        }
    }

    // 자동 로그인 기능
    private fun loginAutoCheck() {
        val sharedPref = getSharedPreferences("UserName", MODE_PRIVATE)
        val sharedPref_userName = sharedPref.getString("User_id", "")
        if(sharedPref_userName != "")
        {
            val intent = Intent(this@LoginActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
        else
        {
            return
        }
    }

    // 접속한 사용자의 아이디 정보를 저장
    fun sharedLoginUpdate(userName : String) {
        val sharedPref = getSharedPreferences("UserName", MODE_PRIVATE)
        val sharedPref_editor = sharedPref.edit()
        sharedPref_editor.putString("User_id", userName)
        sharedPref_editor.commit()
    }
}