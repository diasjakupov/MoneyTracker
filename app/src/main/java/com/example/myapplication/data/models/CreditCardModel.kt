package com.example.myapplication.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity("credit_card")
data class CreditCardModel(
    var name: String,
    var type: String,
    var cardNumber: String,
    var balance: Int,
    var color: String,
    @PrimaryKey(autoGenerate = true) var id: Int = 0
){

}
