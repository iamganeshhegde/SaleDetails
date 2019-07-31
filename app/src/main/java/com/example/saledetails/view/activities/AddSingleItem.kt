package com.example.saledetails.view.activities

import android.app.Activity
import android.os.Bundle
import android.text.Editable
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.saledetails.R
import kotlinx.android.synthetic.main.activity_add_single_item.*
import kotlinx.android.synthetic.main.activity_new_sales.*

class AddSingleItem : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_add_single_item)

        btnAddSingleProduct.setOnClickListener {

            if (etProductName.text != null && etProductName.text.toString() != "" && etQuantity.text != null && etQuantity.text.toString() != "" && etRate.text != null && etRate.text.toString() != "") {
                sendItemDetailsToNewSaleActivity(
                    etProductName.text.toString(),
                    etQuantity.text.toString(),
                    etRate.text.toString()
                )

            } else {
                Toast.makeText(this, "Please fill all details", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun sendItemDetailsToNewSaleActivity(productName: String, quantity: String, rate: String) {
        intent.putExtra("productName", productName)
        intent.putExtra("quantity", quantity)
        intent.putExtra("rate", rate)
        setResult(Activity.RESULT_OK, intent)
        finish()

    }

}
