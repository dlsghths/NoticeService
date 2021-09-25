package com.healthcare.noticeservice

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
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

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if(context is Activity)
        {
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

        firebase_database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var arraylist_data = ArrayList<String>()

                for(postSnapshot in snapshot.child("의료용").children) {
                    arraylist_data.add(postSnapshot.key.toString() + ", " + postSnapshot.value.toString())
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
                var position = p2.toString()
                val alert_builder = AlertDialog.Builder(activity)
                    .setTitle("알림 저장")
                    .setMessage("개인 알림 인증서에 저장하겠습니까?")
                    .setNegativeButton("취소", null)
                    .setPositiveButton("저장") { DialogInterface, i ->
                        // TODO 데이터베이스에 사용자 선택 요소 추가

                    }
                    .show()

                return true
            }
        })

        return rootView
    }
}