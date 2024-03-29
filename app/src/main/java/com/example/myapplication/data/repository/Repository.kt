package com.example.myapplication.data.repository

import com.example.myapplication.data.models.Cheque
import com.example.myapplication.data.models.CreditCardModel
import com.example.myapplication.data.models.RemoteCheque
import com.example.myapplication.data.models.TransactionModel
import com.example.myapplication.data.network.NetResponse
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface Repository {
    fun getAllCreditCards(): Flow<List<CreditCardModel>>
    suspend fun createCard(
        cardName: String,
        type: String,
        cardNumber: String,
        color: Int
    )

    suspend fun updateCard(
        cardName: String,
        type: String,
        cardNumber: String,
        color: Int, cardId: Int
    )

    suspend fun deleteCardById(id: Int)
    suspend fun deleteChequeByCardID(cardId: Int)


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

    suspend fun getRemoteCheque(url: String): Flow<NetResponse<RemoteCheque?>>
}