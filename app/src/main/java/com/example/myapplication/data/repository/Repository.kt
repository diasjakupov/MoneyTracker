package com.example.myapplication.data.repository

import com.example.myapplication.data.models.Cheque
import com.example.myapplication.data.models.CreditCardModel
import com.example.myapplication.data.models.TransactionModel
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import java.util.*

interface Repository {
    fun getAllCreditCards(): Flow<List<CreditCardModel>>
    suspend fun createCard(
        cardName: String,
        type: String,
        cardNumber: String,
        color: ULong
    )

    suspend fun updateCard(
        cardName: String,
        type: String,
        cardNumber: String,
        color: ULong, cardId: Int
    )

    suspend fun deleteCardById(id: Int)


    suspend fun createCheque(
        cardId: Int,
        transactions: List<TransactionModel>,
        date: LocalDate
    )
    suspend fun updateCheque(
        cardId: Int,
        transactions: List<TransactionModel>,
        date: LocalDate,
        chequeId: Int
    )

    fun getAllCheques(cardId: Int): Flow<List<Cheque>>
    suspend fun deleteChequeById(idx: Int)
}