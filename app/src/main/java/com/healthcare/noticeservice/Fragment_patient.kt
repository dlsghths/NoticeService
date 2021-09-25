package com.healthcare.noticeservice

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
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
import org.jetbrains.annotations.NotNull
import java.util.ArrayList

class Fragment_patient : Fragment() {

    val database = Firebase.database
    var activity = Activity()
    var userName : String? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is Activity) {
            activity = context
        }
    }

    @NotNull
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        arguments?.let {
            userName = it.getString("userName").toString()
        }
        val rootView = inflater.inflate(R.layout.fragment__all, container, false)
        val listView_hospital = rootView.findViewById<ListView>(R.id.fragment_listView_all)


        val databaseRef = database.getReference()
        databaseRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var arraylist_data = ArrayList<String>()

                for (postSnapshot in snapshot.child("대학교").children) {
                    arraylist_data.add(postSnapshot.key.toString() + ", " + postSnapshot.value.toString())
                }

                val adapter_first = ArrayAdapter<String>(
                    activity,
                    android.R.layout.simple_list_item_1,
                    arraylist_data
                )
                listView_hospital.adapter = adapter_first
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
                    .setPositiveButton("저장") {DialogInterface, i ->
                        val action_activity = getActivity()
                        val fragment_select = Fragment_user()

                        val bundle = Bundle()
                        bundle.putString("userName", "${userName}")
                        fragment_select.arguments = bundle
                        action_activity?.supportFragmentManager?.beginTransaction()?.replace(R.id.frameLayout, fragment_select)?.commit()
                    }
                    .show()

                return true
            }
        })
        return rootView
    }

    private fun newInstance(): Fragment_patient {
        val args = Bundle()
        val frag = Fragment_patient()
        frag.arguments = args
        return frag
    }
}
