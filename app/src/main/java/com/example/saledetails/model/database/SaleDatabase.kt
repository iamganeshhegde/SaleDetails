package com.example.saledetails.model.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.saledetails.model.database.dao.ItemsDao
import com.example.saledetails.model.database.dao.SaleDao
import com.example.saledetails.model.database.dao.SaleItemsDao
import com.example.saledetails.model.database.entity.Items
import com.example.saledetails.model.database.entity.Sale

@Database(entities = arrayOf(Sale::class, Items::class),version = 1)
abstract class SaleDatabase: RoomDatabase() {
    abstract fun saleDao(): SaleDao
    abstract fun itemsDao(): ItemsDao
    abstract fun saleAndItemsDao(): SaleItemsDao
}