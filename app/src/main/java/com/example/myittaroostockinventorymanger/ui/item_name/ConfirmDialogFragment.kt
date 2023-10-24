package com.example.myittaroostockinventorymanger.ui.item_name

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
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.switchMap
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.myittaroostockinventorymanger.Application
import com.example.myittaroostockinventorymanger.R
import com.example.myittaroostockinventorymanger.databinding.ConfirmDialogFragmentBinding
import com.example.myittaroostockinventorymanger.event.Event
import com.example.myittaroostockinventorymanger.util.ImageShower
import com.squareup.picasso.Picasso

class ConfirmDialogFragment : DialogFragment() {

    private lateinit var alertDialog: AlertDialog
    private lateinit var context: Context
    private lateinit var selectedIdList: List<Long>
    private lateinit var binding: ConfirmDialogFragmentBinding
    private val arg: ConfirmDialogFragmentArgs by navArgs()
    private lateinit var existingIdList: List<Long>
    private val viewModel: ConfirmDialogFragmentViewModel by viewModels()
    private var itemIdsRelatedToBatch: List<Long> = listOf()
    private lateinit var adapter: WarningRecycleViewAdapter

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val layoutInflater = layoutInflater
        binding = ConfirmDialogFragmentBinding.inflate(layoutInflater, null, false)
        val view = binding.root

        val context = getContext()

        if (context != null) {
            this.context = context
        }

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

        hideView()

        //set up recyView
        setUpRecycleView(context)

        viewModel.getAllExistItemIdFromBatch()
            .observe(this) {
                existingIdList = it
                viewModel.checkIsItemRelatedToBatch(selectedIdList, it)
            }

        //check for item have related batch data if have delete all data
        viewModel.getIsItemRelatedToBatch
            .observe(this) {
                if (it) {
                    viewModel.deleteBatchesByItemIds(itemIdsRelatedToBatch)
                    viewModel.deleteItems(selectedIdList)
                } else {
                    viewModel.deleteItems(selectedIdList)
                }
                alertDialog.cancel()
            }

        //get item's related data show Warning
        viewModel.getItemIdsRelatedToBatch.switchMap {
            itemIdsRelatedToBatch = it
            viewModel.getBatchWithItemByItemIds(it)
        }.observe(this) {
            adapter.insertData(it)
            binding.txtBody.text = getText(R.string.warning_message)
            showView()
        }

        //set num of delete item
        viewModel.numOfDeleteItems(selectedIdList.size)

        viewModel.getNumOfDeleteItem()
            .observe(this) {
                binding.txtTitle.text = it
            }

        //to delete stock
        binding.btnYes.setOnClickListener { v: View ->
            viewModel.checkIsValidDelete(selectedIdList, existingIdList)
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

    private fun showView() {
        binding.recyWarning.visibility = View.VISIBLE
        binding.txtWarningTitle.visibility = View.VISIBLE
    }

    private fun hideView() {
        binding.recyWarning.visibility = View.GONE
        binding.txtWarningTitle.visibility = View.GONE
    }

    private fun setUpRecycleView(context: Context) {
        adapter = WarningRecycleViewAdapter()

        binding.recyWarning.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = this@ConfirmDialogFragment.adapter
        }
    }
}