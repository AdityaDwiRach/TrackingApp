package com.adr.trackingapp.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable


@Entity(tableName = "history_table")
data class HistoryEntity (
    //TODO tentuin fix isi tabel sebelum jalanin tes
    val date: String,
    val distance: Double,
    val description: String,
    val screenshot: ByteArray,
    val averageSpeed: Double
):Serializable{
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}