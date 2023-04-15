package com.example.myapplication.data.repository

import android.util.Log
import com.example.myapplication.data.dao.ChequeDao
import com.example.myapplication.data.dao.CreditCardDao
import com.example.myapplication.data.datasource.RemoteDataSource
import com.example.myapplication.data.models.Cheque
import com.example.myapplication.data.models.CreditCardModel
import com.example.myapplication.data.models.RemoteCheque
import com.example.myapplication.data.models.TransactionModel
import com.example.myapplication.data.network.NetResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.time.LocalDate
import javax.inject.Inject


class RepositoryImpl @Inject constructor(
    private val creditCardDao: CreditCardDao,
    private val chequeDao: ChequeDao,
    private val remoteDataSource: RemoteDataSource
) : Repository {

    //Credit cards
    override fun getAllCreditCards(): Flow<List<CreditCardModel>> {
        return creditCardDao.getAllCreditCards()
    }
    override suspend fun createCard(
        cardName: String,
        type: String,
        cardNumber: String,
        color: Int
    ) {

        creditCardDao.createCreditCard(
            CreditCardModel(
                cardName,
                type,
                cardNumber,
                0,
                color
            )
        )


    }
    override suspend fun updateCard(
        cardName: String,
        type: String,
        cardNumber: String,
        color: Int,
        cardId: Int
    ) {
        creditCardDao.updateCreditCard(
            CreditCardModel(
                cardName,
                type,
                cardNumber,
                0,
                color,
                cardId
            )
        )
    }
    override suspend fun deleteCardById(id: Int) {
        creditCardDao.deleteCreditCard(id)
    }

    //Cheques
    override suspend fun deleteChequeByCardID(cardId: Int) {
        chequeDao.deleteByCardId(cardId)
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
    override suspend fun getRemoteCheque(url: String): Flow<NetResponse<RemoteCheque?>> {
        return flow{
            emit(NetResponse.Loading<RemoteCheque?>())
            val response = remoteDataSource.getCheque(url).execute()
            if(response.isSuccessful){
                emit(NetResponse.Successful(response.body()))
            }else{
                emit(NetResponse.Error(response.errorBody().toString()))
            }
        }

    }

}