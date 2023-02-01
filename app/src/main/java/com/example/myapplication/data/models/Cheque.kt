package com.example.myapplication.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate


@Entity("cheque")
data class Cheque(
    val transactions: List<TransactionModel>,
    val date: LocalDate,
    val total: Double,
    val cardId: Int,
    @PrimaryKey(autoGenerate = true) var id: Int = 0
){

}
