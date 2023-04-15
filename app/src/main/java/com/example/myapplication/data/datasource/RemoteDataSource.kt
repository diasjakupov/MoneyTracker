package com.example.myapplication.data.datasource

import com.example.myapplication.data.models.RemoteCheque
import com.example.myapplication.data.network.ParseApi
import retrofit2.Call
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val parseApi: ParseApi
) {

    suspend fun getCheque(url: String): Call<RemoteCheque>{
        return parseApi.getChequeData(url.replace("/", "_"))
    }
}