package com.adr.trackingapp.database

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.adr.trackingapp.data.model.HistoryEntity
import io.reactivex.CompletableObserver
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class HistoryRepository {

    private val compositeDisposable = CompositeDisposable()
    private var databaseInstance: HistoryDatabase? = null
    private var dataList: List<HistoryEntity>? = null
    private var context: Context? = null

    fun setDatabaseInstance(databaseInstance: HistoryDatabase, context: Context) {
        this.databaseInstance = databaseInstance
        this.context = context
    }

    fun insertData(historyEntity: HistoryEntity) {
        databaseInstance?.historyDAO()?.insert(historyEntity)
            ?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe(object : CompletableObserver {
                override fun onComplete() {
                    Toast.makeText(context, "Data successfully inserted.", Toast.LENGTH_SHORT).show()
                }

                override fun onSubscribe(d: Disposable) {
                    compositeDisposable.add(d)
                }

                override fun onError(e: Throwable) {
                    TODO("Not yet implemented")
                }

            })
    }

    fun deleteData(historyEntity: HistoryEntity) {
        databaseInstance?.historyDAO()?.delete(historyEntity)
            ?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe(object : CompletableObserver {
                override fun onComplete() {
                    Toast.makeText(context, "Data successfully deleted.", Toast.LENGTH_SHORT).show()
                }

                override fun onSubscribe(d: Disposable) {
                    compositeDisposable.add(d)
                }

                override fun onError(e: Throwable) {
                    TODO("Not yet implemented")
                }

            })
    }

    fun getAllData(): List<HistoryEntity>? {
        databaseInstance?.historyDAO()?.getAllData()
            ?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe(object: Observer<List<HistoryEntity>>{
                override fun onComplete() {
                    Log.d("DatabaseViewModel", "all data have been retrieved")
                }

                override fun onSubscribe(d: Disposable) {
                    compositeDisposable.add(d)
                }

                override fun onNext(t: List<HistoryEntity>) {
                    dataList = t
                }

                override fun onError(e: Throwable) {
                    TODO("Not yet implemented")
                }

            })
        return dataList
    }
}