package com.example.myittaroostockinventorymanger.ui.batch

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.myittaroostockinventorymanger.Application
import com.example.myittaroostockinventorymanger.R
import com.example.myittaroostockinventorymanger.data.entities.BatchWithItem
import com.example.myittaroostockinventorymanger.databinding.FragmentAddAndUpdateBatchBinding
import com.example.myittaroostockinventorymanger.enum.Option
import com.example.myittaroostockinventorymanger.util.ImageShower
import com.example.myittaroostockinventorymanger.util.DateInputMask
import java.text.SimpleDateFormat

class AddAndUpdateBatchFragment : Fragment(), MenuProvider {

    private lateinit var binding: FragmentAddAndUpdateBatchBinding
    private val args: AddAndUpdateBatchFragmentArgs by navArgs()
    private val viewModel: AddNewAndUpdateBatchViewModel by viewModels()
    private var option: String = ""
    private var batchId: Long = 0
    private val context = Application.getContext()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentAddAndUpdateBatchBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        batchId = args.batchId

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)

        viewModel.checkInsertOrUpdateBatch(batchId)

        //for show default image
        ImageShower.showImage(context, "", binding.imgViewItemBatch)

        viewModel.getAllItemNames()
            ?.observe(viewLifecycleOwner) {
                Log.d("tag", "onViewCreated: $it ")
                setUpAutoCompleteTextView(it)
            }

        //Todo consider something possible things
        viewModel.getOption()
            .observe(viewLifecycleOwner) {
                if (it.equals(Option.UPDATE_ITEM.name)) {
                    viewModel.findBatchWithItemById(batchId)
                }
                option = it
            }

        viewModel.getIsValidBatch()
            .observe(viewLifecycleOwner) {
                val validBatch = it.contentIfNotHandle

                if (validBatch != null) {

                    when (option) {
                        Option.NEW_ITEM.name -> viewModel.insertBatch(validBatch)

                        else -> viewModel.updateBatch(validBatch)
                    }
                }
            }

        viewModel.insertedBatchWithItem
            .observe(viewLifecycleOwner) {
                val transaction = viewModel.createTransaction(it)
                viewModel.insertTransaction(transaction)
            }

        viewModel.existingBatch
            .observe(viewLifecycleOwner) {
                populateData(it)
                Log.d("tag", "onViewCreated: ${it.batch.batchId}")
            }

        viewModel.isReturnBack()
            .observe(viewLifecycleOwner) {
                val notify = it.contentIfNotHandle

                if (notify != null) {
                    if (notify) {
                        findNavController().popBackStack()
                    }
                }
            }

        viewModel.getMessage()
            .observe(viewLifecycleOwner) {
                val notify = it.contentIfNotHandle
                if (notify != null) {
                    Toast.makeText(Application.getContext(), notify, Toast.LENGTH_SHORT).show()
                }
            }
//todo learn textWatcher future
        DateInputMask(binding.edtDate)

        binding.actItemName.onItemClickListener =
            OnItemClickListener { p0, p1, p2, p3 ->
                val itemName = binding.actItemName.text.toString()
                viewModel.findItemByName(itemName)
                    .observe(viewLifecycleOwner) {
                        //after type load image to imageView
                        Log.d("myTag", "onItemClick: $it")
                        ImageShower.showImage(context, it.imagePath, binding.imgViewItemBatch)
                    }
            }

    }

    private fun populateData(batchWithItem: BatchWithItem) {
        val dateFormat = SimpleDateFormat("dd/MM/YYYY")
        val batch = batchWithItem.batch
        val item = batchWithItem.item
        val expDate = dateFormat.format(batch.expDate)
        binding.actItemName.setText(item.name)
        binding.edtDate.setText(expDate)
        binding.edtQuantity.setText(batch.quantity.toString())
        binding.edtCostPrice.setText(batch.basePrice.toString())
        binding.edtSalePrice.setText(batch.sellingPrice.toString())
        //load image to imageView
        ImageShower.showImage(context, item.imagePath, binding.imgViewItemBatch)
    }


    //AutoCompleteTextView for item name
    private fun setUpAutoCompleteTextView(itemNameList: List<String>) {
        val adapter = ArrayAdapter(
            context,
            R.layout.simple_drop_down_layout,
            R.id.txt_name,
            itemNameList
        )
        //type first char show drop down
        binding.actItemName.threshold = 1
        binding.actItemName.setAdapter(adapter)
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.add_new_item_app_bar, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {

        // todo for test
        when (menuItem.itemId) {
            R.id.save -> viewModel.onClickSave(
                option,
                batchId,
                binding.actItemName,
                binding.edtDate,
                binding.edtQuantity,
                binding.edtCostPrice,
                binding.edtSalePrice,
                binding.edtDate
            )
        }
        return false
    }
}