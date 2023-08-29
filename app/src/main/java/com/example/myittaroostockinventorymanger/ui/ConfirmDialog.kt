package com.example.myittaroostockinventorymanger.ui

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.example.myittaroostockinventorymanger.R

class ConfirmDialog private constructor() : DialogFragment() {

    lateinit var txtTitle: TextView

    lateinit var btnYes: Button

    lateinit var btnNo: Button

    private lateinit var callBack: CallBack
    private lateinit var alertDialog: AlertDialog
    private lateinit var key: String

    companion object {

        fun getNewInstance(key: String, num: Int): ConfirmDialog {
            val confirmDialog = ConfirmDialog()
            val bundle = Bundle()
            bundle.putInt(key, num)
            confirmDialog.arguments = bundle
            return confirmDialog
        }
    }


    fun setKey(key: String) {
        this.key = key
    }

    fun setCallBack(callBack: CallBack) {
        this.callBack = callBack
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val view = layoutInflater.inflate(R.layout.confirm_dialog_fragment, null, false)

        txtTitle = view.findViewById(R.id.txt_title)
        btnYes = view.findViewById(R.id.btn_yes)
        btnNo = view.findViewById(R.id.btn_no)

        alertDialog = AlertDialog.Builder(context)
                .setView(view)
                .setCancelable(false)
                .create()

        alertDialog.window?.setBackgroundDrawableResource(R.drawable.rounded_rectangle_white)

        val bundle = arguments

        if (bundle != null) {
            val num = bundle.getInt(key)
            txtTitle.text = "Delete $num items?"
        }

        return alertDialog
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        btnYes.setOnClickListener {
            callBack.onClickedYes()
            alertDialog.cancel()
        }

        btnNo.setOnClickListener {
            alertDialog.cancel()
        }

        return super.onCreateView(inflater, container, savedInstanceState)
    }

    interface CallBack {

        fun onClickedYes()
    }
}