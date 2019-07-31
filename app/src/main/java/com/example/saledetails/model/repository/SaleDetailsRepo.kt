package com.example.saledetails.model.repository

import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.saledetails.model.database.DatabaseClient
import com.example.saledetails.model.database.dao.SaleAndAllItems
import com.example.saledetails.model.database.dao.SaleDao
import com.example.saledetails.model.database.dao.SaleItemsDao
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.Callable

class SaleDetailsRepo(context: Context) {


    lateinit var saleAndItemsDao: SaleItemsDao
    lateinit var saleDao: SaleDao
    var db = DatabaseClient.getInstance(context)
    var listOfSaleAndItems: List<SaleAndAllItems>? = null
    var mutableSaleData: MutableLiveData<List<SaleAndAllItems>>? = null
    var isDataDeleted: MutableLiveData<Boolean>? = null
    var isDataUpdated: MutableLiveData<Boolean>? = null


    init {

        mutableSaleData = MutableLiveData<List<SaleAndAllItems>>()
        isDataDeleted = MutableLiveData<Boolean>()
        isDataUpdated = MutableLiveData<Boolean>()
        saleAndItemsDao = db.getDatabase().saleAndItemsDao()
        saleDao = db.getDatabase().saleDao()

    }

    fun getAllSalesData(): LiveData<List<SaleAndAllItems>>? {


        Observable.fromCallable(object : Callable<List<SaleAndAllItems>> {
            override fun call(): List<SaleAndAllItems>? {

                listOfSaleAndItems = saleAndItemsDao.loadAllItemsOfSale()
                return listOfSaleAndItems
            }
        })
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<List<SaleAndAllItems>> {
                override fun onComplete() {
                }

                override fun onSubscribe(d: Disposable) {


                }

                override fun onNext(t: List<SaleAndAllItems>) {


                    if (t != null) {
                        mutableSaleData?.value = t
                    }
                }

                override fun onError(e: Throwable) {

                    e.printStackTrace()
                }

            })
        return mutableSaleData

    }


    fun updateSalesData(saleAndAllData: SaleAndAllItems):LiveData<Boolean>? {


        Observable.fromCallable {
            saleDao.update(saleAndAllData.sale)
        }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<Unit> {
                override fun onComplete() {
                }

                override fun onSubscribe(d: Disposable) {
                }

                override fun onNext(t: Unit) {
                    isDataUpdated?.value = true
                    Log.i(TAG,"Updated")
                }

                override fun onError(e: Throwable) {
                    isDataUpdated?.value = false
                    e.printStackTrace()
                }
            })
        return isDataUpdated
    }

    fun deleteSelectedSale(saleData: SaleAndAllItems):LiveData<Boolean>? {

        Observable.fromCallable {
            saleDao.delete(saleData.sale)
        }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<Unit> {
                override fun onComplete() {
                }
                override fun onSubscribe(d: Disposable) {
                }
                override fun onNext(t: Unit) {
                    isDataDeleted?.value = true
                    Log.i(TAG,"Deleted")
                }
                override fun onError(e: Throwable) {
                    isDataDeleted?.value = false
                }
            })
        return isDataDeleted
    }
}
