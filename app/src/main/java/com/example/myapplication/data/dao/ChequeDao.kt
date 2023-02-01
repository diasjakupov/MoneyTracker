package com.example.myapplication.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.myapplication.data.models.Cheque
import kotlinx.coroutines.flow.Flow


@Dao
interface ChequeDao {


    @Query("SELECT * FROM cheque WHERE cardId =:cardId ORDER BY date DESC")
    fun getAllCheque(cardId: Int): Flow<List<Cheque>>


    @Insert
    suspend fun createCheque(cheque: Cheque)

    @Update
    suspend fun updateCheque(cheque: Cheque)

    @Query("DELETE FROM cheque WHERE id=:idx")
    suspend fun deleteById(idx:Int)
}