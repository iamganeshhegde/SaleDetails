package com.example.saledetails.model.database.dao

import androidx.room.Embedded
import androidx.room.Relation
import com.example.saledetails.model.database.entity.Items
import com.example.saledetails.model.database.entity.Sale

class SaleAndAllItems {


    @Embedded
    lateinit var sale: Sale

    @Relation(parentColumn = "id",entityColumn = "userId", entity = Items::class)
    lateinit var list:List<Items>




}