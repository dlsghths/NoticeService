package com.healthcare.noticeservice

import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
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

        val editText_login = findViewById<EditText>(R.id.editText_login);
        val button_login = findViewById<Button>(R.id.button_login);
        var editText_login_userId: String

        val database = Firebase.database

        // 자동 로그인 기능
        login_auto_Check()

        // 로그인 버튼
        button_login.setOnClickListener(View.OnClickListener {
            // 사용자 정보가 있는지 database에 확인 요청
            editText_login_userId = editText_login.text.toString()

            val databaseRef = database.getReference()

            databaseRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    var user = snapshot.child("사용자").child(editText_login_userId).getValue()

                    // 사용자 정보가 있을 경우
                    if(user != null)
                    {
                        // 사용자가 원하는 정보 자르기
                        var array = user.toString().split(",")

                        var arrayData = ArrayList<String>()

                        // 전체 데이터에 대해서 요구할 경우
                        if(array[0] == "전부")
                        {
                            for(postSnapshot in snapshot.child("대학교").children)
                            {
                                arrayData.add(postSnapshot.key.toString() + ", " + postSnapshot.value.toString())
                            }
                        }
                        // 특정 데이터들만 요구할 경우
                        else
                        {
                            var count = 0
                            for(postSnapshot in array)
                            {
                                arrayData.add(array[count] + ", " + snapshot.child("대학교").child(array[count]).getValue().toString())
                                count++
                            }
                        }

                        // MainActivity 호출
                        val intent = Intent(this@LoginActivity, MainActivity::class.java)
                        intent.putExtra("userName", editText_login_userId)
                        intent.putExtra("userInfo", user.toString())
                        intent.putExtra("arrayData", arrayData)

                        // 사용자 아이디 저장장
                       startActivity(intent)
                        finish()
                    }
                    // 사용자 정보가 없을 경우
                    else
                    {
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
        })
    }

    // 자동 로그인 기능
    fun login_auto_Check() {
        val sharedPref = getSharedPreferences("UserName", MODE_PRIVATE)
        val sharedPref_userName = sharedPref.getString("User_id", "")
        if(sharedPref_userName != "")
        {
            // 사용자 정보를 가지고 있을 경우
            val intent = Intent(this@LoginActivity, MainActivity::class.java)
            intent.putExtra("userName", sharedPref_userName)
            startActivity(intent)
            finish()
        }
        else
        {
            return
        }
    }

    // 접속한 사용자의 아이디 정보를 저장
    fun sharedPref_login_update(userName : String) {
        val sharedPref = getSharedPreferences("UserName", MODE_PRIVATE)
        val sharedPref_editor = sharedPref.edit()
        sharedPref_editor.putString("User_id", userName)
        sharedPref_editor.commit()
    }
}