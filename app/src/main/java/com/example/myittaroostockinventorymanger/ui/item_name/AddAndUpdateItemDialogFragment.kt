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
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.example.myittaroostockinventorymanger.R
import com.example.myittaroostockinventorymanger.data.entities.Item
import com.example.myittaroostockinventorymanger.databinding.AddAndUpdateItemDialogFragmentBinding
import com.example.myittaroostockinventorymanger.enum.Option
import com.example.myittaroostockinventorymanger.event.Event

class AddAndUpdateItemDialogFragment : DialogFragment() {

    private lateinit var alertDialog: AlertDialog
    private lateinit var context: Context
    private val addAndUpdateItemViewModel: AddAndUpdateItemViewModel by viewModels()
    private lateinit var binding: AddAndUpdateItemDialogFragmentBinding
    private val args: AddAndUpdateItemDialogFragmentArgs by navArgs()
    private lateinit var item: Item
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val layoutInflater = LayoutInflater.from(getContext())
        val view = layoutInflater.inflate(R.layout.add_and_update_item_dialog_fragment, null, false)

        binding = AddAndUpdateItemDialogFragmentBinding.bind(view)

        alertDialog = AlertDialog.Builder(getContext())
            .setView(view)
            .create()
        alertDialog.getWindow()!!.setBackgroundDrawableResource(R.drawable.rounded_rectangle_white)
        return alertDialog
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val itemId = args.itemId

        val option = if (itemId > 0) {
            // TODO: editing item
            addAndUpdateItemViewModel.findItemById(itemId)
            Option.UPDATE_ITEM
        } else {
            // TODO: add new item
            Option.NEW_ITEM
        }

        addAndUpdateItemViewModel.getItem()
            .observe(this) {
                binding.edtStockName.setText(it.name)
                this.item = it
            }


        addAndUpdateItemViewModel.getIsValidItem
            .observe(this) {

                val item = it.contentIfNotHandle

                if (item != null) {

                    when (option) {

                        Option.NEW_ITEM -> addAndUpdateItemViewModel.insertItem(item)

                        else -> addAndUpdateItemViewModel.updateItemName(item)
                    }

                    alertDialog.dismiss()
                }
            }


        binding.btnSave.setOnClickListener { v: View? ->
            val itemName = binding.edtStockName.text.toString()

            this.item = when (option) {

                Option.NEW_ITEM -> Item(itemName)

                else -> {
                    this.item.name = itemName
                    this.item
                }
            }

            addAndUpdateItemViewModel.onClickDone(item)
        }

        binding.btnCancel.setOnClickListener { v: View? -> alertDialog.dismiss() }


//        to show message update,added or error
        addAndUpdateItemViewModel.getMessage()
            .observe(this) { message: Event<String> ->
                val notify = message.contentIfNotHandle
                if (notify != null) {
                    Toast.makeText(context, notify, Toast.LENGTH_SHORT).show()
                }
            }
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.context = context
    }
}