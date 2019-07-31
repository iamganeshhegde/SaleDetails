package com.example.saledetails.view.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.CalendarView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.saledetails.R
import com.example.saledetails.model.database.entity.Items
import com.example.saledetails.model.database.entity.Sale
import com.example.saledetails.view.adapter.ItemsAdapter
import com.example.saledetails.viewmodel.NewSaleViewModel
import kotlinx.android.synthetic.main.activity_new_sales.*
import java.lang.Exception
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*
import kotlin.collections.ArrayList


class NewSaleActivity : AppCompatActivity() {

    val REQUEST_CODE = 100
    var productName: String? = null
    var productQuantity: Int = 0
    var productRate: Int = 0
    var TAG = NewSaleActivity::class.java.name
    var list = ArrayList<Items>()
    lateinit var newSaleViewModel: NewSaleViewModel
    var totalAmountToBePaidForSingleItem: Int = 0
    var totalAmountOfAllItems: Int = 0
    var customerName: String? = null
    lateinit var date: String
    var customerId: Long = 0
    var balanceAmount: Int = 0
    lateinit var adapter: ItemsAdapter
    var totalQuantityOfAllItems = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_new_sales)

        newSaleViewModel = ViewModelProviders.of(this).get(NewSaleViewModel::class.java)

        init()


        fabAddSingleItem.setOnClickListener {
            startActivityForResult(Intent(this, AddSingleItem::class.java), REQUEST_CODE)
        }

        setRecyclerView()


        etPaidCustomerMoney.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                if (s != null && s != "") {
                    try {
                        cbCustomerPaidFull.isChecked = Integer.parseInt(s?.trim().toString()) == totalAmountOfAllItems
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }

            }
        })

    }

    private fun init() {

        customerId = System.currentTimeMillis()

        var currentDate = getCurrentDateTime()
        var dateString = currentDate.toString("dd-MM-yyyy")

        date = dateString

        tvDate.text = dateString

        btnSaveDetails.setOnClickListener {

            var saleModel = Sale()
            getAllSaleAndItemDetails(saleModel);

            if (list != null && etCustomerName.text.toString() != "" && etPaidCustomerMoney.text.toString() != "" && list.size > 0 && totalAmountOfAllItems >= Integer.parseInt(
                    etPaidCustomerMoney.text.toString()
                )
            ) {
                newSaleViewModel.insertDataToDatabase(saleModel, list)

                setResult(Activity.RESULT_OK)
                finish()
            } else {
                Toast.makeText(this, "Please enter all details correctly", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getCurrentDateTime(): Date {
        return Calendar.getInstance().time
    }

    fun Date.toString(format: String, locale: Locale = Locale.getDefault()): String {
        val formatter = SimpleDateFormat(format, locale)
        return formatter.format(this)
    }


    private fun getAllSaleAndItemDetails(saleModel: Sale) {
        if (etPaidCustomerMoney.text.toString() != null && etPaidCustomerMoney.text.toString() != "") {
            var moneyPaidByCustomers = Integer.parseInt(etPaidCustomerMoney.text.toString())
            balanceAmount = totalAmountOfAllItems - moneyPaidByCustomers;

            saleModel.balanceAmount = balanceAmount.toString()
            saleModel.id = customerId
            saleModel.date = date
            saleModel.name = etCustomerName.text.toString()

            saleModel.totalAmount = totalAmountOfAllItems.toString()
        }
    }

    private fun setRecyclerView() {
        rvItems.isNestedScrollingEnabled = false
        rvItems.layoutManager = LinearLayoutManager(this)
        adapter = ItemsAdapter(this)
        rvItems.adapter = adapter
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_CODE -> {

                    productName = data?.getStringExtra("productName")

                    productQuantity = Integer.parseInt(data?.getStringExtra("quantity"))
                    productRate = Integer.parseInt(data?.getStringExtra("rate"))

                    getTotalAmount()
                }
            }
        }
    }

    private fun getTotalAmount() {

        var totalAmountWithoutGST = productQuantity * productRate

        var cgst: Double = ((totalAmountWithoutGST * 9) / 100).toDouble()
        var sgst: Double = ((totalAmountWithoutGST * 9) / 100).toDouble()

        Log.i(TAG, "CGST " + cgst.toString())
        Log.i(TAG, "SGST " + sgst.toString())

        totalAmountToBePaidForSingleItem = (totalAmountWithoutGST + cgst + sgst).toInt()


        var item: Items = Items()
        item.itemName = productName.toString()
        item.quantity = productQuantity
        item.rate = productRate.toDouble()
        item.totalAmount = totalAmountToBePaidForSingleItem.toDouble()
        item.userId = customerId

        list.add(item)
        adapter.setListItems(list)
        adapter.notifyItemInserted(list.size)


        tvSubTotal.text = "Sub Total"

        totalAmountOfAllItems += totalAmountToBePaidForSingleItem

        //display total
        tvTotalAmoutAll.text = totalAmountOfAllItems.toString()

        //quantity total
        totalQuantityOfAllItems += productQuantity

        tvTotalItems.text = totalQuantityOfAllItems.toString()

        Log.i(TAG, "TOTAL " + totalAmountToBePaidForSingleItem.toString())
        Log.i(TAG, "productName " + productName.toString())
        Log.i(TAG, "productQuantity " + productQuantity.toString())
        Log.i(TAG, "productRate " + productRate.toString())
        Log.i(TAG, "customerId " + customerId.toString())
    }
}
