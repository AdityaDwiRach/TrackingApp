package com.adr.trackingapp.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.adr.trackingapp.data.model.HistoryEntity

@Database(entities = [HistoryEntity::class], version = 1)
abstract class HistoryDatabase : RoomDatabase() {

    abstract fun historyDAO(): HistoryDAO
    companion object {
        private var INSTACE: HistoryDatabase? = null

        fun getInstance(context: Context): HistoryDatabase?{
            if (INSTACE == null){
                synchronized(HistoryDatabase::class){
                    INSTACE = Room.databaseBuilder(context.applicationContext,
                        HistoryDatabase::class.java, "history_database.db")
                        .build()
                }
            }
            return INSTACE
        }

        fun destroyInstace() {
            INSTACE = null
        }
    }
}