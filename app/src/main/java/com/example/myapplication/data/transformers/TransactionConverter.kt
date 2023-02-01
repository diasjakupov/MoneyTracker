package com.example.myapplication.data.transformers

import androidx.room.TypeConverter
import com.example.myapplication.data.models.TransactionModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class TransactionConverter {


    @TypeConverter
    fun toTransactionList(value: String): List<TransactionModel> {
        return Gson().fromJson(value, object : TypeToken<List<TransactionModel>>() {}.type)
    }


    @TypeConverter
    fun fromTransactionList(value: List<TransactionModel>): String{
        return Gson().toJson(value)
    }
}