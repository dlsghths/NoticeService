package com.healthcare.noticeservice

import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class Healthcare_database {
    val database = Firebase.database

    // 사용자 정보 만들기
    /*
     사용자 정보를 만들기 위한 값 필요
     userName : 사용자가 사용할 아이디
     select_hospital : 사용자가 알림을 제공받으려 선택한 병원 리스트
     */
    /*
    fun database_send(userName:String, select_hospital:String) {
        var databaseRef = database.getReference();

        // databaseRef.child("사용자").child("사용자4").setValue("test Message");
        databaseRef.child("사용자").child(userName).setValue(select_hospital);
    }
    */

    // 로그인 버튼 클릭시 사용자 정보 확인하기
    /*
    로그인 버튼 클릭시 요청한 사용자에 대한 정보가 있는지 확인
     */
    fun database_loginCheck(userName: String) {
        val databaseRef = database.getReference()

        databaseRef.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var user = snapshot.child("사용자").child(userName)
                Log.d("tag", user.toString())
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("tag", "읽어오기 실패")
            }
        })

    }

    // 사용자 정보 받아오기
    /*
     사용자가 선택한 병원 리스트 받아오기
     */
    fun database_receive(userName: String) {
        val databaseRef = database.getReference();

        /*
        databaseRef.child("사용자").child("사용자2").get().addOnSuccessListener {
            System.out.println("${it.value}")
        }
         */
        databaseRef.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var user = snapshot.child("사용자").child(userName)
                Log.d("tag", user.toString())
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("tag", "읽어오기 실패")
            }
        })
    }
}