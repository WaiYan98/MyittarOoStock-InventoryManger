package com.example.myittaroostockinventorymanger.ui.batch

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.example.myittaroostockinventorymanger.Application
import com.example.myittaroostockinventorymanger.R
import com.example.myittaroostockinventorymanger.databinding.ConfirmDialogFragmentBinding

class ConfirmDialogFragment : DialogFragment() {

    private lateinit var context: Context
    private lateinit var alertDialog: AlertDialog
    private lateinit var binding: ConfirmDialogFragmentBinding
    private var selectedIdList: List<Long> = arrayListOf()
    private val args: ConfirmDialogFragmentArgs by navArgs()
    private val viewModel: ConfirmDialogViewModel by viewModels()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = ConfirmDialogFragmentBinding.inflate(layoutInflater, null, false)
        val view = binding.root
        context = Application.getContext()

        selectedIdList = args.selectedIdList.toList()
        Log.d("myTag", "onCreateDialog: $selectedIdList")

        alertDialog = AlertDialog.Builder(getContext())
            .setView(view)
            .create()

        alertDialog.window
            ?.setBackgroundDrawableResource(R.drawable.rounded_rectangle_white)

        return alertDialog
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        viewModel.setTitle(selectedIdList.size)

        viewModel.getTitle()
            .observe(this) {
                binding.txtTitle.text = it
            }

        binding.btnYes.setOnClickListener {
            viewModel.onClickYes(selectedIdList)
            alertDialog.cancel()
        }

        binding.btnNo.setOnClickListener {
            alertDialog.cancel()
        }

        viewModel.getMessage()
            .observe(this) {
                val notify = it.contentIfNotHandle
                if (notify != null) {
                    Toast.makeText(context, notify, Toast.LENGTH_SHORT).show()
                }
            }
        return super.onCreateView(inflater, container, savedInstanceState)
    }

}