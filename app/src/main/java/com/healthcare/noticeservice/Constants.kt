package com.healthcare.noticeservice

import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class Constants {
    companion object {
        val Notification_ID = 0
        lateinit var user_name : String

        fun get() : String {
            return user_name
        }
        fun set(value : String) {
            this.user_name = value
        }
    }
    /*
    SharedPreferences 정보
    1. UserName
    - 최신 접속 사용자의 아이디를 저장한다.
     */
}