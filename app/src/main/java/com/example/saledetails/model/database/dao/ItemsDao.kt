package com.example.saledetails.model.database.dao

import androidx.room.*
import com.example.saledetails.model.database.entity.Items

@Dao
interface ItemsDao {

    @Insert
    fun insertItems(item: List<Items>)

    @Update
    fun updateItems(item: Items)

    @Delete
    fun deleteItems(item: Items)

    @Query("SELECT *FROM items")
    fun getAllItems():List<Items>

}
