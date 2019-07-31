package com.example.saledetails.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.saledetails.R
import com.example.saledetails.model.database.entity.Items
import kotlinx.android.synthetic.main.item_new_adapter.view.*

class ItemsAdapter(
    context: Context
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var listOfItems: List<Items> = ArrayList<Items>()
    lateinit var context: Context
    val HEADER_TYPE = 0
    val ITEM_TYPE = 1

    init {
        this.context = context
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(getItemViewType(position) == ITEM_TYPE){

            (holder as MyViewHolder).tvItemName.text = listOfItems.get(position-1).itemName
            (holder as MyViewHolder).tvQuantity.text = listOfItems.get(position-1).quantity.toString()
            (holder as MyViewHolder).tvRate.text = listOfItems.get(position-1).rate.toString()
            (holder as MyViewHolder).tvTotalAmount.text = listOfItems.get(position-1).totalAmount.toString()
            (holder as MyViewHolder).tvId.text = (position).toString()
        }
    }



    fun setListItems(list:List<Items>){
        this.listOfItems = list
    }

    override fun getItemViewType(position: Int): Int {

        if (position == 0) {
            return HEADER_TYPE

        } else {
            return ITEM_TYPE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        if (viewType == HEADER_TYPE) {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_header, parent, false)
            return MyHeaderViewHolder(view)
        } else {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_new_adapter, parent, false)
            return MyViewHolder(view)
        }
    }

    override fun getItemCount(): Int {
        if(listOfItems.isEmpty()){
            return 0
        }
        return listOfItems.size+1
    }


    class MyHeaderViewHolder(view: View?) : RecyclerView.ViewHolder(view!!) {
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvItemName = itemView.tvName
        var tvQuantity = itemView.tvQuantity
        var tvRate = itemView.tvRate
        var tvTotalAmount = itemView.tvTotalAmount
        var tvId = itemView.tvId

    }
}