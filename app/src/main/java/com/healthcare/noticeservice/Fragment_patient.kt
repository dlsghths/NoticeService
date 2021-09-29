package com.healthcare.noticeservice

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import org.jetbrains.annotations.NotNull
import java.util.ArrayList

class Fragment_patient : Fragment() {

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
        val rootView = inflater.inflate(R.layout.fragment__patient, container, false)
        val listView_hospital = rootView.findViewById<ListView>(R.id.fragment_listView_patient)

        val firebase_database = Firebase.database.getReference()
        val sharedPref = context?.getSharedPreferences("UserName", MODE_PRIVATE)
        val sharedPrefUserName = sharedPref?.getString("User_id", "").toString()

        // 커스텀리스트뷰
        val customAdapter = CustomListViewAdapter()

        firebase_database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // arraylist 초기화
                arraylist_data.clear()

                listView_hospital.adapter = customAdapter

                for (postSnapshot in snapshot.child("환자용").children) {
                    arraylist_data.add(postSnapshot.key.toString() + ", " + postSnapshot.value.toString())
                    customAdapter.addItem(postSnapshot.key.toString() + ", " + postSnapshot.value.toString())
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })

        // listView의 item을 선택했을 때의 이벤트 처리
        listView_hospital.setOnItemClickListener { p0, p1, position, p3 ->

        }

/*
        listView_hospital.setOnItemLongClickListener { p0, p1, position, p3 ->
            val alert_builder = AlertDialog.Builder(activity)
                .setTitle("알림 저장")
                .setMessage("개인 알림 인증서에 저장하겠습니까?")
                .setNegativeButton("취소", null)
                .setPositiveButton("저장") { DialogInterface, i ->

                    // 추가한 알람 정보를 SharedPreferences에 저장
                    val sharedPref = activity?.getSharedPreferences("hospital", MODE_PRIVATE)
                    val sharedPref_editor = sharedPref?.edit()

                    val array = arraylist_data.get(position).toString().split(", ")
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
                                val array_hospital = "${it.value}".split(", ")
                                var exist_hospital = false
                                var count = 0
                                for (i in array_hospital) {
                                    if (array[0] == array_hospital[count]) {
                                        exist_hospital = true
                                    }
                                    count++
                                }

                                if (exist_hospital == false) {
                                    // 같은 이름의 데이터가 없을 경우 데이터 추가 실행
                                    val add_data = "${it.value}" + ", " + array[0]
                                    firebase_database.child("사용자").child(sharedPrefUserName)
                                        .setValue(add_data)
                                } else {
                                    // 같은 이름이 데이터가 있을 경우 데이터 추가 실행 안함
                                }
                            }
                        }
                }
                .show()

            true
        }
*/


        return rootView
    }

    fun test () {
        CustomListViewAdapter().getViewInvisible()
        customAdapter.notifyDataSetChanged()
    }

    // MainActivity에서 item 선택시 호출
    fun optionsItemSelected(item: MenuItem) {
        when {
            // item이 "선택"일 경우
            // checkBox를 보이게

            // item이 "저장"일 경우
            // checkBox를 안보이게
            test()
        }
    }
}
