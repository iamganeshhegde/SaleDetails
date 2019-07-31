package com.example.saledetails.model.database.dao

import androidx.room.*
import com.example.saledetails.model.database.entity.Sale

@Dao
interface SaleDao {

    @Query("SELECT *FROM sale")
    fun getAll(): List<Sale>

    @Update
    fun update(sale: Sale)

    @Insert
    fun insert(sale: Sale)

    @Delete
    fun delete(sale: Sale)

}