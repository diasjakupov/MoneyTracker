package com.example.myapplication.data.models

import android.icu.util.LocaleData
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate
import java.util.Date


@Entity("transactions")
data class TransactionModel(
    val name: String,
    val date: LocalDate,
    val price: Double,
    val cardId: Int
){
    @PrimaryKey(autoGenerate = true) var id: Int = 0
}
