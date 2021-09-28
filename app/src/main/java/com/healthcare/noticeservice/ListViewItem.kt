package com.healthcare.noticeservice

import android.graphics.drawable.Drawable

class ListViewItem {
    private lateinit var text : String

    fun setText(text : String) {
        this.text = text
    }

    fun getText() : String {
        return this.text
    }
}