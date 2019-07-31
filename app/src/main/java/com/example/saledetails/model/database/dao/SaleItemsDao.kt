package com.example.saledetails.model.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction

@Dao
interface SaleItemsDao {

//    @Query("SELECT *FROM sale WHERE id=:id")
    @Query("SELECT *FROM sale")
    @Transaction
    fun loadAllItemsOfSale():List<SaleAndAllItems>?


    @Query("SELECT *FROM sale WHERE id=:id")
    @Transaction
    fun loadAllItemsOfSaleOfSingleSale(id:Long):SaleAndAllItems

}