package com.example.myittaroostockinventorymanger.ui.batch

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
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
import com.example.myittaroostockinventorymanger.util.TextWatcherTest
import java.text.SimpleDateFormat

class AddAndUpdateBatchFragment : Fragment(), MenuProvider {

    //    var txtInputLayout: TextInputLayout
//    private var addNewAndUpdateBatchViewModel: AddNewAndUpdateBatchViewModel
//    private val stockNameList: List<String> = ArrayList()
    private lateinit var binding: FragmentAddAndUpdateBatchBinding
    private val args: AddAndUpdateBatchFragmentArgs by navArgs()
    private val viewModel: AddNewAndUpdateBatchViewModel by viewModels()
    private var option: String = ""
    private var batchId: Long = 0


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

        TextWatcherTest(binding.edtDate)


//        binding.actItemName.setOnTouchListener(OnTouchListener { v: View, motion: MotionEvent ->
//            binding.actItemName.showDropDown()
//            false
//        })

    }

    private fun populateData(batchWithItem: BatchWithItem) {
        val dateFormat = SimpleDateFormat("dd/MM/YYYY")
        val batch = batchWithItem.batch
        val item = batchWithItem.item
        val expDate = dateFormat.format(batch.expDate)
        binding.actItemName.setText(item.name)
        binding.edtDate.setText(expDate)
        binding.edtQuantity.setText(batch.quantity.toString())
        binding.edtCostPrice.setText(batch.originalPrice.toString())
        binding.edtSalePrice.setText(batch.salePrice.toString())
    }

//test
//        edtDate.listen();


//stockBatch batchId is always 0 so I request batchId from batchFragment for testing
//        // TODO: 2/5/2022 need to check later
//        val intent = intent
//        if (intent != null) {
//            option = intent.getStringExtra(EXTRA_OPTION)
//            itemBatch = intent.getParcelableExtra(EXTRA_STOCK_BATCH)
//            batchId = intent.getLongExtra(EXTRA_BATCH_ID, 0)
//        }
//        if (option == UPDATE) {
//            val dateFormat: DateFormat = SimpleDateFormat("dd/MM/YYYY")
//            val expDate = dateFormat.format(itemBatch!!.batch.expDate)
//            actStockName.setText(itemBatch!!.item.name)
//            //            edtDate.setText(expDate);
//            edtAmount.setText(itemBatch!!.batch.quantity.toString())
//            edtCostPrice.setText(itemBatch!!.batch.originalPrice.toString())
//            edtSalePrice.setText(itemBatch!!.batch.salePrice.toString())
//        }
//        addNewAndUpdateBatchViewModel!!.getAllStockNames()
//            .observe(
//                this,
//                Observer { stockNameList: List<String> -> setUpAutoCompleteTextView(stockNameList) })
//        actStockName.setOnTouchListener(OnTouchListener { v: View?, event: MotionEvent? ->
//            actStockName.showDropDown()
//            false
//        })
//        toolBar.setNavigationOnClickListener(View.OnClickListener { v: View? -> goToMainActivity() })
//        toolBar.setOnMenuItemClickListener(Toolbar.OnMenuItemClickListener { item: MenuItem ->
//            if (item.itemId == R.id.save) {
//
//                // TODO: 8/25/2023   handle edtExpDate
//                addNewAndUpdateBatchViewModel!!.onClickSave(
//                    option!!, batchId!!,
//                    actStockName, null, edtAmount, edtCostPrice, edtSalePrice, true
//                )
//            }
//            false
//        })
//        addNewAndUpdateBatchViewModel!!.getMessage()
//            .observe(this, Observer<Event<String?>> { mEvent: Event<String?> ->
//                val notify = mEvent.contentIfNotHandle
//                if (notify != null) {
//                    Toast.makeText(this, notify, Toast.LENGTH_SHORT).show()
//                }
//            })
//        addNewAndUpdateBatchViewModel!!.getNavigateToMainActivity()
//            .observe(this, Observer<Event<Boolean?>> { navigateEvent: Event<Boolean?> ->
//                val navigate = navigateEvent.contentIfNotHandle
//                if (navigate != null) {
//                    goToMainActivity()
//                }
//            })


    //AutoCompleteTextView for item name
    private fun setUpAutoCompleteTextView(itemNameList: List<String>) {
        val adapter = ArrayAdapter(
            Application.getContext(),
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