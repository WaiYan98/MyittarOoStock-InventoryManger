package com.example.myittaroostockinventorymanger.ui.item_name

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.example.myittaroostockinventorymanger.Application
import com.example.myittaroostockinventorymanger.R
import com.example.myittaroostockinventorymanger.databinding.ConfirmDialogFragmentBinding
import com.example.myittaroostockinventorymanger.event.Event

class ConfirmDialogFragment : DialogFragment() {

    private lateinit var alertDialog: AlertDialog
    private lateinit var context: Context
    private lateinit var selectedIdList: List<Long>
    private lateinit var binding: ConfirmDialogFragmentBinding
    private val arg: ConfirmDialogFragmentArgs by navArgs()


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val layoutInflater = layoutInflater
        binding = ConfirmDialogFragmentBinding.inflate(layoutInflater, null, false)
        val view = binding.root

        context = Application.getContext()
        selectedIdList = arg.selectedIdList.toList()

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
        val viewModel = ViewModelProvider(requireActivity())
            .get(ConfirmDialogFragmentViewModel::class.java)

        //set num of delete item
        viewModel.numOfDeleteItems(selectedIdList.size)

        viewModel.getNumOfDeleteItem()
            .observe(this) {
                binding.txtTitle.text = it
            }

        //to delete stock
        binding.btnYes.setOnClickListener { v: View ->
            viewModel.checkIsValidDelete(selectedIdList)
            alertDialog.cancel()
        }

        binding.btnNo.setOnClickListener { v: View -> alertDialog.cancel() }


        //to show message
        viewModel.getMessage()
            .observe(this) { m: Event<String> ->
                val message = m.contentIfNotHandle
                if (message != null) {
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                }
            }
        return super.onCreateView(inflater, container, savedInstanceState)
    }
}