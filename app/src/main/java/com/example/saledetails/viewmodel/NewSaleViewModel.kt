package com.example.saledetails.viewmodel

import android.app.Application
import android.content.ContentValues.TAG
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.example.saledetails.model.database.DatabaseClient
import com.example.saledetails.model.database.dao.ItemsDao
import com.example.saledetails.model.database.dao.SaleDao
import com.example.saledetails.model.database.entity.Items
import com.example.saledetails.model.database.entity.Sale
import com.example.saledetails.model.repository.NewSalesRepo
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import java.util.*
import java.util.concurrent.Callable
import kotlin.collections.ArrayList

class NewSaleViewModel(application:Application): AndroidViewModel(application) {


    var newSalesRepo = NewSalesRepo(application)



    fun insertDataToDatabase(
        saleModel: Sale,
        list: ArrayList<Items>
    ) {
        newSalesRepo.insertSalesAndItem(saleModel,list)



       /* Observable.fromCallable {

            return@fromCallable itemsDao.getAllItems()
        }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : io.reactivex.Observer<List<Items>> {
                override fun onComplete() {


                }

                override fun onSubscribe(d: Disposable) {

                }

                override fun onNext(t: List<Items>) {

                    Log.i(TAG,t.get(0).itemName)

                }

                override fun onError(e: Throwable) {

                }

            })*/

        /*Observable.fromCallable(object : Callable<Sale> {
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

                    Log.i(TAG,t.name)
                }

                override fun onError(e: Throwable) {

                    e.printStackTrace()

                }

            })*/

       /* Observable.fromCallable(Callable<Any> {
            salesDAo.insert(saleModel)
            itemsDao.insertItems(list)
            return@Callable saleModel
        })
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).doOnError {
                Log.i(TAG, it.toString())
            }
            .subscribe (

            )*/

        /*  Observable.fromCallable {

              return@fromCallable saleModel {
                  salesDAo.insert(saleModel)
                  itemsDao.insertItems(list)
              }
          }.subscribeOn(Schedulers.io())
              .observeOn(AndroidSchedulers.mainThread())
              .subscribe(object : Consumer<Sale> {
                  override fun accept(t: Sale?) {

                      Log.i(TAG, t?.name)

                  }
              })*/
    }

    fun displayData() {
        newSalesRepo.displayData()


    }
}