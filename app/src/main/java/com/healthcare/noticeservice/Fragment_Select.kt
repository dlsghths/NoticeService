package com.healthcare.noticeservice

import android.app.Activity
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.transition.TransitionInflater.from
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.LayoutInflater.from
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat.getSystemService
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class Fragment_Select : Fragment() {

    val database = Firebase.database
    var activity = Activity()
    var userName : String? = null
    val CHANNL_ID = "Notice_Channel_01"

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if(context is Activity)
        {
            activity = context
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        arguments?.let {
            userName = it.getString("userName").toString()
        }
        //val userName = savedInstanceState?.getString("userName").toString()
        val rootView = inflater.inflate(R.layout.fragment__select, container, false)
        val listView_hospital = rootView.findViewById<ListView>(R.id.fragment_listView_select)

        val databaseRef = database.getReference()
        databaseRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var user = snapshot.child("사용자").child("${userName}").getValue()
                var array = user.toString().split(",")
                var arrayData = ArrayList<String>()

                if(array[0] == "없음")
                {
                    arrayData.add("개인 확인용 데이터를 추가하세요.")
                }
                else
                {
                    var count = 0
                    for(postSnapshot in array)
                    {
                        arrayData.add(array[count] + ", " + snapshot.child("대학교").child(array[count]).getValue().toString())
                        count++
                    }
                }

                var adapter = ArrayAdapter<String>(activity, android.R.layout.simple_list_item_1, arrayData)
                listView_hospital.adapter = adapter

                // createNotificationChannel()
                alarm_calling()
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })

        return rootView
    }

    private fun newInstant() : Fragment_Select
    {
        val args = Bundle()
        val frag = Fragment_Select()
        frag.arguments = args
        return frag
    }

    private fun createNotificationChannel() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            val name = "병원 인증서 알람 서비스"
            val descriptionText = "인증서 기간 연장 필요"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNL_ID, name, importance).apply {
                description = descriptionText
            }
            val notificationManager : NotificationManager =
                getActivity()?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun alarm_calling() {
        val group_Key = "healthcare_alarm_service"

        var message_builder = NotificationCompat.Builder(activity, CHANNL_ID)
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setWhen(System.currentTimeMillis() + 60000)
            .setContentTitle("테스트 메시지")
            .setContentText("테스트 메시지를 확인합니다")
            .setDefaults(Notification.DEFAULT_ALL)
            .setGroup(group_Key)
            .setGroupSummary(true)
            .build()

        NotificationManagerCompat.from(activity).apply {
            notify(0, message_builder)
        }
    }
}