package com.example.myapplication.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.myapplication.data.dao.ChequeDao
import com.example.myapplication.data.dao.CreditCardDao
import com.example.myapplication.data.models.Cheque
import com.example.myapplication.data.models.CreditCardModel
import com.example.myapplication.data.models.TransactionModel
import com.example.myapplication.data.transformers.DateConverter
import com.example.myapplication.data.transformers.TransactionConverter

@Database(
    entities = [CreditCardModel::class, TransactionModel::class, Cheque::class],
    version = 1
)
@TypeConverters(DateConverter::class, TransactionConverter::class)
abstract class MyDatabase : RoomDatabase() {
    abstract fun creditCardDao(): CreditCardDao
    abstract fun chequeDao(): ChequeDao

}