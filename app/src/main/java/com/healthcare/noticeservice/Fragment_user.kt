package com.healthcare.noticeservice

import android.app.Activity
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class Fragment_user : Fragment() {

    var userName : String? = null
    var fragment_activity = Activity()

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if(context is Activity)
        {
            fragment_activity = context
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        arguments?.let {
            userName = it.getString("userName").toString()
        }
        val rootView = inflater.inflate(R.layout.fragment__user, container, false)
        val listView_hospital = rootView.findViewById<ListView>(R.id.fragment_listView_user)

        val firebase_database = Firebase.database.getReference()

        firebase_database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val user_data = snapshot.child("사용자").child("${userName}").getValue()
                val user_data_array = user_data.toString().split(", ")
                val arraylist_data = ArrayList<String>()

                if(user_data_array[0] == "없음")
                {
                    arraylist_data.add("개인 확인용 데이터를 추가하세요.")
                }
                else
                {
                    var count = 0
                    for(postSnapshot in user_data_array)
                    {
                        arraylist_data.add(user_data_array[count] + ", " + snapshot.child("대학교").child(user_data_array[count]).getValue().toString())
                        count++
                    }
                }

                val adapter = ArrayAdapter<String>(fragment_activity, android.R.layout.simple_list_item_1, arraylist_data)
                listView_hospital.adapter = adapter
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })

        return rootView
    }
}