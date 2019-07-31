package com.example.saledetails.model.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(foreignKeys = arrayOf(ForeignKey(entity = Sale::class,
    parentColumns = arrayOf("id"),
    childColumns = arrayOf("userId"),
    onDelete = ForeignKey.CASCADE)))
class Items {

    @ColumnInfo(name = "userId")
    var userId:Long = 0

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "itemId")
    var itemId:Int = 0

    @ColumnInfo(name = "itemName")
    lateinit var itemName:String

    @ColumnInfo(name = "quantity")
    var quantity:Int = 0

    @ColumnInfo(name = "rate")
    var rate:Double = 0.0

    @ColumnInfo(name = "totalAmount")
    var totalAmount:Double = 0.0



}