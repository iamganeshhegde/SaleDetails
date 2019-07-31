package com.example.saledetails.model.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
class Sale : Serializable {

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id")
    var id: Long = 0

    @ColumnInfo(name = "name")
    lateinit var name:String


    @ColumnInfo(name="totalAmount")
    lateinit var totalAmount:String

    @ColumnInfo(name = "balanceAmount")
    lateinit var balanceAmount:String

    @ColumnInfo(name = "date")
    lateinit var date:String




}