package com.example.myittaroostockinventorymanger.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import butterknife.BindView
import butterknife.ButterKnife
import com.example.myittaroostockinventorymanger.R

class ConfirmDialog private constructor() : DialogFragment() {

    @BindView(R.id.txt_title)
    lateinit var txtTitle: TextView

    @BindView(R.id.btn_yes)
    lateinit var btnYes: Button

    @BindView(R.id.btn_no)
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

        ButterKnife.bind(this, view)

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