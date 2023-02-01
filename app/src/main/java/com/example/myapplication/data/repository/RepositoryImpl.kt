package com.example.myapplication.data.repository

import android.util.Log
import com.example.myapplication.data.dao.ChequeDao
import com.example.myapplication.data.dao.CreditCardDao
import com.example.myapplication.data.models.Cheque
import com.example.myapplication.data.models.CreditCardModel
import com.example.myapplication.data.models.TransactionModel
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import java.util.*
import javax.inject.Inject


class RepositoryImpl @Inject constructor(
    private val creditCardDao: CreditCardDao,
    private val chequeDao: ChequeDao
) : Repository {
    override fun getAllCreditCards(): Flow<List<CreditCardModel>> {
        return creditCardDao.getAllCreditCards()
    }

    override suspend fun createCard(
        cardName: String,
        type: String,
        cardNumber: String,
        color: ULong
    ) {

        creditCardDao.createCreditCard(
            CreditCardModel(
                cardName,
                type,
                cardNumber,
                0,
                color.toString()
            )
        )


    }


    override suspend fun updateCard(
        cardName: String,
        type: String,
        cardNumber: String,
        color: ULong,
        cardId: Int
    ) {
        creditCardDao.updateCreditCard(
            CreditCardModel(
                cardName,
                type,
                cardNumber,
                0,
                color.toString(),
                cardId + 1
            )
        )
    }

    override suspend fun deleteCardById(id: Int) {
        creditCardDao.deleteCreditCard(id)
    }


    override suspend fun createCheque(
        cardId: Int,
        transactions: List<TransactionModel>,
        date: LocalDate
    ) {
        val price = transactions.sumOf { it.price }
        chequeDao.createCheque(Cheque(transactions, date, price, cardId))
    }

    override suspend fun updateCheque(
        cardId: Int,
        transactions: List<TransactionModel>,
        date: LocalDate,
        chequeId: Int
    ) {
        val price = transactions.sumOf { it.price }
        chequeDao.updateCheque(Cheque(transactions, date, price, cardId, chequeId))
    }

    override fun getAllCheques(cardId: Int): Flow<List<Cheque>> {
        return chequeDao.getAllCheque(cardId)
    }

    override suspend fun deleteChequeById(idx: Int) {
        chequeDao.deleteById(idx)
    }

}