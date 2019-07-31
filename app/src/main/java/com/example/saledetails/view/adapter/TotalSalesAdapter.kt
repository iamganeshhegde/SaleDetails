package com.example.saledetails.view.adapter

import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.saledetails.R
import com.example.saledetails.model.database.dao.SaleAndAllItems
import kotlinx.android.synthetic.main.item_total_sales.view.*

class TotalSalesAdapter(
    context: Context,
    onClickUpdate: OnClickUpdate
) : RecyclerView.Adapter<TotalSalesAdapter.MyViewHolder>() {

    var context: Context
    var lisTOfSales: List<SaleAndAllItems>? = null
    lateinit var onClickUpdate: OnClickUpdate


    init {
        this.context = context
        this.onClickUpdate = onClickUpdate
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_total_sales, parent, false)
        return MyViewHolder(view)

    }

    override fun getItemCount(): Int {
        if (lisTOfSales != null) {
            return lisTOfSales!!.size

        }
        return 0
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        holder.tvCustomerName.text = lisTOfSales?.get(position)?.sale!!.name
        holder.tvDate.text = lisTOfSales!!.get(position).sale.date
        holder.tvTotalAmount.text = "Total Amount :" + lisTOfSales!!.get(position).sale.totalAmount
        holder.tvBalanceAmount.text = "Balance :" + lisTOfSales!!.get(position).sale.balanceAmount
        holder.setData(lisTOfSales!!.get(position), onClickUpdate);
    }

    fun updateRecyclerView(lisTOfSales: List<SaleAndAllItems>?) {
        this.lisTOfSales = lisTOfSales

    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        init {
            itemView.btnDelete.setOnClickListener(this)
            itemView.btnPay.setOnClickListener(this)
        }

        var saleData = SaleAndAllItems()
        lateinit var onClickUpdate: OnClickUpdate

        override fun onClick(v: View?) {

            when (v) {
                itemView.btnPay -> {

                    onClickUpdate.updateListData(saleData)

                }
                itemView.btnDelete -> {

                    onClickUpdate.deleteSaleData(saleData)

                    Log.i(TAG, "delete Button clicked")

                }
            }
        }

        fun setData(
            get: SaleAndAllItems,
            onClickUpdate: OnClickUpdate
        ) {
            saleData = get
            this.onClickUpdate = onClickUpdate
        }

        val tvCustomerName = itemView.tvCustomerName
        val tvDate = itemView.tvSaleDate
        val tvTotalAmount = itemView.tvTotalAmount
        val tvBalanceAmount = itemView.tvBalanceAmount
    }

    interface OnClickUpdate {
        fun updateListData(saleData: SaleAndAllItems)
        fun deleteSaleData(saleData: SaleAndAllItems)
    }

}