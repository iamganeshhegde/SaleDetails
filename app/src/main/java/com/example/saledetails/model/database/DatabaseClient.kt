package com.example.saledetails.model.database

import android.content.Context
import androidx.room.Room

class DatabaseClient(context: Context) {

    var context: Context = context
    lateinit var saleDatabase: SaleDatabase
    lateinit var appDatabase: SaleDatabase

    init {
            appDatabase = Room.databaseBuilder(context, SaleDatabase::class.java, "saleDb").build()
    }

    companion object {
        var mDatabaseClient: DatabaseClient? = null
        fun getInstance(context: Context): DatabaseClient {

            if (mDatabaseClient == null) {
                mDatabaseClient = DatabaseClient(context)
            }
            return mDatabaseClient as DatabaseClient
        }
    }

    fun getDatabase(): SaleDatabase {
        return appDatabase
    }
}