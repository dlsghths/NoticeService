package com.healthcare.noticeservice

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible

class CustomListViewAdapter : BaseAdapter() {
    private val listViewItemList = ArrayList<ListViewItem>()
    lateinit var convertView : View
    override fun getCount(): Int {
        return listViewItemList.size
    }

    override fun getView(position: Int, view: View?, parent: ViewGroup?): View? {
        val context = parent?.context
        if (view != null) {
            convertView = view
        }

        if (convertView == null) {
            val inflater = context?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertView = inflater.inflate(R.layout.checkableframelayout, parent, false)
        }

        val textTitleView = convertView?.findViewById<TextView>(R.id.list_contents)

        val listViewItem = listViewItemList[position]
        textTitleView?.setText(listViewItem.getText())

        return convertView
    }

    fun getViewInvisible() {
        val checkBox = convertView?.findViewById<CheckBox>(R.id.list_checkBox)
        checkBox.isVisible = false
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItem(position: Int): Any {
        return listViewItemList[position]
    }

    fun addItem(textTitle: String) {
        val item = ListViewItem()
        item.setText(textTitle)

        listViewItemList.add(item)
    }
}