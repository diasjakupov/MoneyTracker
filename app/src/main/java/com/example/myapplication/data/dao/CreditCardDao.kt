package com.example.myapplication.data.dao

import androidx.room.*
import com.example.myapplication.data.models.CreditCardModel
import kotlinx.coroutines.flow.Flow


@Dao
interface CreditCardDao {

    @Query("SELECT * FROM credit_card")
    fun getAllCreditCards(): Flow<List<CreditCardModel>>


    @Insert
    suspend fun createCreditCard(card: CreditCardModel)

    @Update
    suspend fun updateCreditCard(card: CreditCardModel)


    @Query("DELETE FROM credit_card WHERE id =:id")
    suspend fun deleteCreditCard(id: Int)
}