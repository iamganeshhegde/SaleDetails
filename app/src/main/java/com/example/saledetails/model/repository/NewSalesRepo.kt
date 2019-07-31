package com.example.saledetails.model.repository

import android.content.ContentValues
import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.saledetails.model.database.DatabaseClient
import com.example.saledetails.model.database.dao.ItemsDao
import com.example.saledetails.model.database.dao.SaleAndAllItems
import com.example.saledetails.model.database.dao.SaleDao
import com.example.saledetails.model.database.dao.SaleItemsDao
import com.example.saledetails.model.database.entity.Items
import com.example.saledetails.model.database.entity.Sale
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.Callable

class NewSalesRepo(context:Context) {
    var itemsData: MutableLiveData<Items> = MutableLiveData<Items>()
    var db = DatabaseClient.getInstance(context)
    lateinit var itemsDao: ItemsDao
    lateinit var salesDAo: SaleDao
    lateinit var saleAndItemsDao:SaleItemsDao
    init {

        itemsDao = db.getDatabase().itemsDao()
        salesDAo = db.getDatabase().saleDao()
        saleAndItemsDao = db.getDatabase().saleAndItemsDao()
    }

    fun insertSalesAndItem(saleModel: Sale,
                           list: ArrayList<Items>){
        Observable.fromCallable(object : Callable<Sale> {
            override fun call(): Sale {

                salesDAo.insert(saleModel)
                itemsDao.insertItems(list)
                return saleModel
            }
        })
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object :io.reactivex.Observer<Sale> {
                override fun onComplete() {
                }

                override fun onSubscribe(d: Disposable) {
                }

                override fun onNext(t: Sale) {
                    Log.i(ContentValues.TAG,t.name)
                }

                override fun onError(e: Throwable) {
                    e.printStackTrace()

                }

            })
    }

    fun displayData() {
        Observable.fromCallable(object : Callable<SaleAndAllItems> {
            override fun call(): SaleAndAllItems {

                var saleItems = saleAndItemsDao.loadAllItemsOfSaleOfSingleSale(1564385420119)
                return saleItems
            }
        })
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object :io.reactivex.Observer<SaleAndAllItems> {
                override fun onComplete() {
                }

                override fun onSubscribe(d: Disposable) {
                }

                override fun onNext(t: SaleAndAllItems) {
                    Log.i(ContentValues.TAG,t.sale.name)
                    Log.i(ContentValues.TAG,t.sale.totalAmount)
                }

                override fun onError(e: Throwable) {
                    e.printStackTrace()

                }

            })

    }


}