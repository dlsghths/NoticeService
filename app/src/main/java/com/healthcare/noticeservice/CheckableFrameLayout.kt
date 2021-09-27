package com.healthcare.noticeservice

import android.content.Context
import android.util.AttributeSet
import android.widget.CheckBox
import android.widget.Checkable
import android.widget.LinearLayout

class CheckableFrameLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : LinearLayout(context, attrs), Checkable {

    // 현재 checkBox의 체크 상태를 리턴
    override fun isChecked(): Boolean {
        val checkBox = findViewById<CheckBox>(R.id.list_checkBox)

        return checkBox.isChecked
    }

    // 현재 checkBox Checked 상태를 바꿈
    override fun toggle() {
        val checkBox = findViewById<CheckBox>(R.id.list_checkBox)

        if (checkBox.isChecked == false) {
            setChecked(true)
        }
        else if (checkBox.isChecked == true) {
            setChecked(false)
        }
    }

    // checkBox의 check 상태를 checked에 따라서 변경
    override fun setChecked(checked : Boolean) {
        val checkBox = findViewById<CheckBox>(R.id.list_checkBox)

        if (checkBox.isChecked != checked) {
            checkBox.setChecked(checked)
        }
    }
}