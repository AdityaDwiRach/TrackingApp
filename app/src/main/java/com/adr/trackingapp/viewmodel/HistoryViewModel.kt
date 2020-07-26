package com.adr.trackingapp.viewmodel

import androidx.lifecycle.ViewModel
import com.adr.trackingapp.database.HistoryEntity
import com.adr.trackingapp.database.HistoryRepository

class HistoryViewModel : ViewModel() {

    private val repository by lazy { HistoryRepository() }
    private val allHistory: List<HistoryEntity> = ArrayList()

    fun insert(historyEntity: HistoryEntity){
        repository.insertData(historyEntity)
    }

    fun delete(historyEntity: HistoryEntity){
        repository.deleteData(historyEntity)
    }

    fun getAll() : List<HistoryEntity>{
        return repository.getAllData() ?: allHistory
    }
}