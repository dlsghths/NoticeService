package com.healthcare.noticeservice

import android.app.*
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.DialogInterface
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
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

    var fragment_activity = Activity()
    val arraylist_data = ArrayList<String>()

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is Activity) {
            fragment_activity = context
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment__user, container, false)
        val listView_hospital = rootView.findViewById<ListView>(R.id.fragment_listView_user)

        val firebase_database = Firebase.database.getReference()
        val sharedPref = context?.getSharedPreferences("UserName", MODE_PRIVATE)
        val sharedPrefUserName = sharedPref?.getString("User_id", "").toString()

        firebase_database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                arraylist_data.clear()
                val user_data = snapshot.child("사용자").child(sharedPrefUserName).getValue()
                val user_data_array = user_data.toString().split(", ")

                if (user_data_array[0] == "없음") {
                    arraylist_data.add("개인 확인용 데이터를 추가하세요.")
                } else {
                    var count = 0
                    for (postSnapshot in user_data_array) {
                        // TODO 사용자가 선택한 요소 의료용, 환자용에 따라서 분류
                        arraylist_data.add(
                            user_data_array[count] + ", " + snapshot.child("환자용")
                                .child(user_data_array[count]).getValue().toString()
                        )
                        count++
                    }
                }

                val adapter = ArrayAdapter<String>(
                    fragment_activity,
                    android.R.layout.simple_list_item_1,
                    arraylist_data
                )
                listView_hospital.adapter = adapter
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })

        listView_hospital.setOnItemLongClickListener(object : AdapterView.OnItemLongClickListener {
            override fun onItemLongClick(
                p0: AdapterView<*>?,
                p1: View?,
                p2: Int,
                p3: Long
            ): Boolean {
                val alert_builder = AlertDialog.Builder(activity)
                    .setTitle("알림 제거")
                    .setMessage("개인 알림 인증서에서 제거하시겠습니까?")
                    .setNegativeButton("취소", null)
                    .setPositiveButton("저장") { DialogInterface, i ->

                        // 삭제한 알림 정보를 SharedPreferences에서 삭제
                        val sharedPref = activity?.getSharedPreferences("hospital", MODE_PRIVATE)
                        val sharedPref_editor = sharedPref?.edit()

                        val array = arraylist_data.get(p2).toString().split(", ")
                        sharedPref_editor?.remove(array[0])
                        sharedPref_editor?.commit()

                        // TODO 데이터베이스에 사용자 선택 요소 제거
                        firebase_database.child("사용자").child(sharedPrefUserName).get()
                            .addOnSuccessListener {
                                val array_hospital = "${it.value}".split(", ")
                                var count = 0
                                for (i in array_hospital[count]) {
                                    // 해당 병원의 이름 찾기
                                    if (array[0] == array_hospital[count]) {
                                        val new_array_hospital = array_hospital - array[0]
                                        var new_add_data = ""
                                        var count2 = 0
                                        for (i2 in new_array_hospital[count2]) {
                                            new_add_data += new_array_hospital[count2]
                                            count2++
                                            // 마지막 for문일 경우
                                            Log.d("tag", new_array_hospital.size.toString())
                                            if (count2 != new_array_hospital.size) {
                                                new_add_data += ", "
                                            } else {
                                                break
                                            }
                                        }
                                        firebase_database.child("사용자").child(sharedPrefUserName)
                                            .setValue(new_add_data)
                                        break
                                    }
                                    count++
                                }
                            }
                    }
                    .show()

                return true
            }
        })
        return rootView
    }
}