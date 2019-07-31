package com.example.saledetails.view.activities

import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.saledetails.R
import com.example.saledetails.model.database.dao.SaleAndAllItems
import com.example.saledetails.view.adapter.TotalSalesAdapter
import com.example.saledetails.viewmodel.SaleDetailsViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.item_paid_amout_by_customer.view.*

class MainActivity : AppCompatActivity(), TotalSalesAdapter.OnClickUpdate {


    private var isBtnFullPaid: Boolean = true
    lateinit var saleDetailsViewModel: SaleDetailsViewModel
    var TAG = MainActivity::class.java.name
    val SALE_REQUEST = 10000
    lateinit var adapter: TotalSalesAdapter
    var listOfFullPaidData: ArrayList<SaleAndAllItems>? = ArrayList<SaleAndAllItems>()
    var listOfPartiallyPaidData: ArrayList<SaleAndAllItems>? = ArrayList<SaleAndAllItems>()
    lateinit var alertDialog: AlertDialog
    var filteredData: ArrayList<SaleAndAllItems> = ArrayList<SaleAndAllItems>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        saleDetailsViewModel = ViewModelProviders.of(this).get(SaleDetailsViewModel::class.java)

        initRecyclerView()

        initSearchData()

        fabAddItems.setOnClickListener {

            startActivityForResult(Intent(this, NewSaleActivity::class.java), SALE_REQUEST)
        }

        btnFullPaid.setOnClickListener {
            isBtnFullPaid = true
            setBackgroundColour(it, btnPartially)
            updateRecyclerView(listOfFullPaidData)
            clearEdittextAndDisplaySearchButton()

        }
        btnPartially.setOnClickListener {

            isBtnFullPaid = false
            setBackgroundColour(it, btnFullPaid)
            updateRecyclerView(listOfPartiallyPaidData)
            clearEdittextAndDisplaySearchButton()
        }
    }

    private fun initSearchData() {
        editTextSearch.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {

                var DRAWABLE_LEFT = 0
                var DRAWABLE_RIGHT = 2

                var compoundDrawables = editTextSearch.compoundDrawables

                var leftDrawable = compoundDrawables[DRAWABLE_LEFT]

                if (event != null) {
                    if (event.action == MotionEvent.ACTION_DOWN) {
                        if (leftDrawable != null) {
                            if (event.rawX <= leftDrawable.bounds.width() + editTextSearch.paddingRight) {
                                if(isBtnFullPaid){
                                    adapter.updateRecyclerView(listOfFullPaidData)
                                }else{
                                    adapter.updateRecyclerView(listOfPartiallyPaidData)
                                }
                                clearEdittextAndDisplaySearchButton()
                                return true
                            }

                        }
                    }
                }
                return false
            }

        })

        editTextSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filteredData.clear()

                if (s != "" && s != null) {
                    if (isBtnFullPaid) {

                        for (i in 0 until (listOfFullPaidData?.size ?: 0)) {

                            if (listOfFullPaidData?.get(i)?.sale?.name?.toLowerCase()?.contains(s.toString().toLowerCase())!!) {

                                filteredData.add(listOfFullPaidData!!.get(i))
                            }

                            adapter.updateRecyclerView(filteredData)
                            adapter.notifyDataSetChanged()
                        }

                    } else {
                        for (i in 0 until (listOfPartiallyPaidData?.size ?: 0)) {
                            if (listOfPartiallyPaidData?.get(i)?.sale?.name?.toLowerCase()?.contains(s.toString().toLowerCase())!!) {

                                filteredData.add(listOfPartiallyPaidData!!.get(i))
                            }

                            adapter.updateRecyclerView(filteredData)
                            adapter.notifyDataSetChanged()

                        }
                    }
                }
            }
        })

        search_list.setOnClickListener {
            search_list.visibility = GONE
            editTextSearch.visibility = VISIBLE
            search_line_view.visibility = VISIBLE
        }

    }

    private fun clearEdittextAndDisplaySearchButton() {
        editTextSearch.text.clear()
        hideKeyBoard()
        editTextSearch.visibility = GONE
        search_list.visibility = VISIBLE
        search_line_view.visibility = GONE
    }

    fun hideKeyBoard() {
        if (currentFocus != null && currentFocus.windowToken != null) {
            val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(currentFocus.windowToken, 0)
        } else {
            try {
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(window.decorView.windowToken, 0)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

    }

    private fun setBackgroundColour(it: View, secondButton: Button) {

        it.setBackgroundColor(resources.getColor(R.color.green))
        secondButton.setBackgroundColor(resources.getColor(android.R.color.white))
    }

    private fun initRecyclerView() {
        adapter = TotalSalesAdapter(this@MainActivity, this)
        rvTotalSales.adapter = adapter
        rvTotalSales.layoutManager = LinearLayoutManager(this@MainActivity, RecyclerView.VERTICAL, false)

        getSaleItemsFromDatabase()

    }

    private fun getSaleItemsFromDatabase() {

        saleDetailsViewModel.getAllSaleDetails()?.observe(this, object : Observer<List<SaleAndAllItems>> {
            override fun onChanged(t: List<SaleAndAllItems>?) {

                updateTheListOfPaidAndPartialPayment(t)
            }
        })

    }

    private fun updateTheListOfPaidAndPartialPayment(list: List<SaleAndAllItems>?) {

        if (list != null) {
            listOfPartiallyPaidData?.clear()
            listOfFullPaidData?.clear()

            for (i in 0..list.size - 1) {
                if (list.get(i).sale.balanceAmount.toFloat() > 0.0) {

                    listOfPartiallyPaidData?.add(list.get(i))
                } else {
                    listOfFullPaidData?.add(list.get(i))
                }
            }

            listOfPartiallyPaidData?.reverse()
            listOfFullPaidData?.reverse()

        }


        if (isBtnFullPaid) {
            updateRecyclerView(listOfFullPaidData)
        } else {
            updateRecyclerView(listOfPartiallyPaidData)
        }

    }

    private fun updateRecyclerView(list: List<SaleAndAllItems>?) {
        adapter.updateRecyclerView(list)
        list?.size?.let { adapter.notifyItemInserted(it) }

        adapter.notifyDataSetChanged()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)


        if (requestCode == SALE_REQUEST && resultCode == Activity.RESULT_OK) {

            getSaleItemsFromDatabase()
        }
    }


    override fun updateListData(saleData: SaleAndAllItems) {

        var layout = LayoutInflater.from(this).inflate(R.layout.item_paid_amout_by_customer, null)
        var alertDialogBuilder: AlertDialog.Builder = AlertDialog.Builder(this)
        alertDialogBuilder.setView(layout)
        alertDialog = alertDialogBuilder.create()


        var etAmountPaid = layout.et_paid_amount
        var btnItemPayUpdate = layout.btnItemPayUpdate
        var btnItemCancel = layout.btnItemCancel
        layout.tvTotalUpdateItem.text = "Total :" + saleData.sale.totalAmount
        layout.tvBalanceUpdateItem.text = "Balance :" + saleData.sale.balanceAmount


        btnItemPayUpdate.setOnClickListener {

            if (etAmountPaid.text.toString() != "" && etAmountPaid.text.toString() != null) {
                checkAndUpdateDatabase(saleData, etAmountPaid)
            } else {
                Toast.makeText(this, "Please enter the valid amount", Toast.LENGTH_SHORT).show()
            }

        }
        btnItemCancel.setOnClickListener {
            alertDialog.cancel()
        }

        if(!isBtnFullPaid){
            alertDialog.show()
        }
    }

    private fun checkAndUpdateDatabase(
        saleData: SaleAndAllItems,
        etAmountPaid: EditText
    ) {

        var amountNewlyPaid: Float = etAmountPaid.text.toString().toFloat()

        var balanceAmount = saleData.sale.balanceAmount.toFloat()
        if (amountNewlyPaid == balanceAmount) {
            saleData.sale.balanceAmount = 0.toString()

            updateDatabaseOfBalance(saleData)
        } else if (amountNewlyPaid < balanceAmount) {

            var balance = balanceAmount - amountNewlyPaid

            saleData.sale.balanceAmount = balance.toString()

            updateDatabaseOfBalance(saleData)

        } else if (amountNewlyPaid > balanceAmount) {
            Toast.makeText(this, "Please enter the valid amount", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateDatabaseOfBalance(saleData: SaleAndAllItems) {
        saleDetailsViewModel.updateSaleDetails(saleData)
        getSaleItemsFromDatabase()

        alertDialog.cancel()
    }

    override fun deleteSaleData(saleData: SaleAndAllItems) {
        Log.i(ContentValues.TAG, "delete Button clicked")

        var deleteAlertDialog: AlertDialog.Builder = AlertDialog.Builder(this)
        deleteAlertDialog.setTitle("Are you sure you want to delete this item?")
            .setPositiveButton("Yes", DialogInterface.OnClickListener { dialog, which ->
                saleDetailsViewModel.deleteSaleDetails(saleData)?.observe(this, object : Observer<Boolean> {
                    override fun onChanged(t: Boolean?) {

                        if (t!!) {
                            Log.i(TAG, "ViewModel   Deleted")
                        }
                    }
                })
                getSaleItemsFromDatabase()

                dialog.dismiss()

            }).setNegativeButton("No", DialogInterface.OnClickListener { dialog, which ->
                dialog.dismiss()
            })

        alertDialog = deleteAlertDialog.create()
        alertDialog.show()
    }
}
