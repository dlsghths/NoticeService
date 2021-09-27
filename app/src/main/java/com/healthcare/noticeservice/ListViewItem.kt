package com.healthcare.noticeservice

import android.graphics.drawable.Drawable

class ListViewItem {
    private lateinit var textTitle : String
    private lateinit var textContent : String

    fun setTextTitle(textTitle : String) {
        this.textTitle = textTitle
    }

    fun getTextTitle() : String {
        return this.textTitle
    }

    fun setTextContent(textContent : String) {
        this.textContent = textContent
    }

    fun getTextContent() : String {
        return this.textContent
    }
}