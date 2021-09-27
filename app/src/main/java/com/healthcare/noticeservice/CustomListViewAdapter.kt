package com.healthcare.noticeservice

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView

class CustomListViewAdapter : BaseAdapter() {
    private val listViewItemList = ArrayList<ListViewItem>()

    override fun getCount(): Int {
        return listViewItemList.size
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
        val pos = position
        val context = parent?.context

        if (convertView == null) {
            val inflater = context?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val convertView = inflater.inflate(R.layout.checkableframelayout, parent, false)
        }

        val textTitleView = convertView?.findViewById<TextView>(R.id.list_title)
        val textContentView = convertView?.findViewById<TextView>(R.id.list_contents)

        val listViewItem = listViewItemList.get(position)

        textTitleView?.setText(listViewItem.getTextTitle())
        textContentView?.setText(listViewItem.getTextContent())

        return convertView
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItem(position: Int): Any {
        return listViewItemList.get(position)
    }

    fun addItem(textTitle: String, textContent: String) {
        val item = ListViewItem()
        item.setTextTitle(textTitle)
        item.setTextContent(textContent)

        listViewItemList.add(item)
    }
}