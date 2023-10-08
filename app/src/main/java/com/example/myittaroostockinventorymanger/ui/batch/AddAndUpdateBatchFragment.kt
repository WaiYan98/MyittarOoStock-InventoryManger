package com.example.myittaroostockinventorymanger.ui.batch

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.myittaroostockinventorymanger.R
import com.example.myittaroostockinventorymanger.data.entities.ItemBatch
import com.example.myittaroostockinventorymanger.event.Event
import com.example.myittaroostockinventorymanger.ui.MainActivity
import com.example.myittaroostockinventorymanger.ui.batch.BatchFragment.Companion.EXTRA_BATCH_ID
import com.example.myittaroostockinventorymanger.ui.batch.BatchFragment.Companion.EXTRA_OPTION
import com.example.myittaroostockinventorymanger.ui.batch.BatchFragment.Companion.EXTRA_STOCK_BATCH
import com.example.myittaroostockinventorymanger.ui.batch.BatchFragment.Companion.UPDATE
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.textfield.TextInputLayout
import java.text.DateFormat
import java.text.SimpleDateFormat

class AddAndUpdateBatchFragment : Fragment() {

    private lateinit var toolBar: MaterialToolbar
    private lateinit var actStockName: AutoCompleteTextView
//    var edtAmount: EditText
//    var edtCostPrice: EditText
//    var edtSalePrice: EditText
//    var txtInputLayout: TextInputLayout
//    private var addNewAndUpdateBatchViewModel: AddNewAndUpdateBatchViewModel
//    private val stockNameList: List<String> = ArrayList()
//    private var option: String
//    private var itemBatch: ItemBatch
//    private var batchId: Long

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_add_and_update_batch, container, false)

        return view


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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


    //AutoCompleteTextView for stock name
//    private fun setUpAutoCompleteTextView(stockNameList: List<String>) {
//        val adapter = ArrayAdapter(
//            this,
//            R.layout.simple_drop_down_layout, R.id.txt_name, stockNameList
//        )
//        actStockName!!.setAdapter(adapter)
//    }

//    private fun setUpViewModel() {
//        addNewAndUpdateBatchViewModel = ViewModelProvider(this)
//            .get(AddNewAndUpdateBatchViewModel::class.java)
//    }
}