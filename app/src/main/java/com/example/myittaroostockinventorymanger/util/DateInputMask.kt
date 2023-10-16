package com.example.myittaroostockinventorymanger.util

import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import java.util.Calendar

class DateInputMask(private val edtDate: EditText) {

    fun listen() {
        edtDate.addTextChangedListener(textWatcher)
    }

    private val textWatcher = object : TextWatcher {

        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            Log.d("myTag", "beforeTextChanged: charSequence = $p0 start = $p1")
            Log.d("myTag", "beforeTextChanged: before = $p2 count = $p3")
        }

        override fun afterTextChanged(p0: Editable?) {
        }

    }

}