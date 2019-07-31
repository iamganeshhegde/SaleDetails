package com.example.saledetails.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.saledetails.model.database.dao.SaleAndAllItems
import com.example.saledetails.model.repository.SaleDetailsRepo

class SaleDetailsViewModel(application: Application): AndroidViewModel(application) {

    lateinit var repo:SaleDetailsRepo
    init {
        repo = SaleDetailsRepo(application)
    }

    fun getAllSaleDetails():LiveData<List<SaleAndAllItems>>?{


        return repo.getAllSalesData()

    }

    fun updateSaleDetails(saleData: SaleAndAllItems):LiveData<Boolean>? {

        return repo.updateSalesData(saleData)
    }


    fun deleteSaleDetails(saleData: SaleAndAllItems):LiveData<Boolean>?{

        return repo.deleteSelectedSale(saleData)
    }
}