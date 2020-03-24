package com.adr.trackingapp.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface HistoryDAO {

    @Insert
    fun insert(historyEntity: HistoryEntity)

    @Query("SELECT * FROM historyentity")
    fun getAllData(): List<HistoryEntity>

    @Delete @Query("SELECT id FROM historyentity")
    fun delete(historyEntity: HistoryEntity)
    //TODO need testing
}