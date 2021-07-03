package com.adr.trackingapp.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.adr.trackingapp.data.model.HistoryEntity
import io.reactivex.Completable
import io.reactivex.Observable

@Dao
interface HistoryDAO {

    @Insert
    fun insert(historyEntity: HistoryEntity): Completable

    @Query("SELECT * FROM history_table")
    fun getAllData(): Observable<List<HistoryEntity>>

    @Delete
    fun delete(historyEntity: HistoryEntity): Completable
}