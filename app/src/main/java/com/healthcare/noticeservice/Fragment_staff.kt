package com.healthcare.noticeservice

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class Fragment_staff : Fragment() {

    var fragment_activity = Activity()
    var arraylist_data = ArrayList<String>()

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is Activity) {
            fragment_activity = context
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment__staff, container, false)
        val listView_hospital = rootView.findViewById<ListView>(R.id.fragment_listView_staff)

        val firebase_database = Firebase.database.getReference()
        val sharedPref = context?.getSharedPreferences("UserName", MODE_PRIVATE)
        val sharedPrefUserName = sharedPref?.getString("User_id", "").toString()

        firebase_database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // arraylist 초기화
                arraylist_data.clear()

                // 커스텀리스트뷰
                val customAdapter = CustomListViewAdapter()
                listView_hospital.adapter = customAdapter

                for (postSnapshot in snapshot.child("의료용").children) {
                    arraylist_data.add(postSnapshot.key.toString() + ", " + postSnapshot.value.toString())
                    customAdapter.addItem(postSnapshot.key.toString() + ", " + postSnapshot.value.toString())
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })

        listView_hospital.setOnItemLongClickListener { p0, p1, position, p3 ->
            val alert_builder = AlertDialog.Builder(activity)
                .setTitle("알림 저장")
                .setMessage("개인 알림 인증서에 저장하겠습니까?")
                .setNegativeButton("취소", null)
                .setPositiveButton("저장") { DialogInterface, i ->

                    // 추가한 알람 정보를 SharedPreferences에 저장
                    val sharedPref = activity?.getSharedPreferences("hospital", MODE_PRIVATE)
                    val sharedPref_editor = sharedPref?.edit()

                    val array = arraylist_data.get(position).split(", ")
                    sharedPref_editor?.putString(array[0], array[1])
                    sharedPref_editor?.commit()

                    // TODO 데이터베이스에 사용자 선택 요소 추가, 중복 저장에 대한 해결 부분 추가
                    firebase_database.child("사용자").child(sharedPrefUserName).get()
                        .addOnSuccessListener {
                            // 기존에 설정한 데이터 값이 없을 경우
                            if ("${it.value}" == "없음") {
                                firebase_database.child("사용자").child(sharedPrefUserName)
                                    .setValue(array[0])
                            }
                            // 기존에 설정한 데이터 값이 있을 경우
                            else {
                                val add_data = "${it.value}" + ", " + array[0]
                                firebase_database.child("사용자").child(sharedPrefUserName)
                                    .setValue(add_data)
                            }
                        }
                }
                .show()

            true
        }
        return rootView
    }

    // MainActivity에서 item 선택시 호출
    fun optionsItemSelected(item: MenuItem) {
        // item이 "선택"일 경우

        // item이 "저장"일 경우
    }
}