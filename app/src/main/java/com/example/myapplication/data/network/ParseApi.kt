package com.example.myapplication.data.network

import com.example.myapplication.data.models.RemoteCheque
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path


interface ParseApi {

    @GET("parse/{url}")
    fun getChequeData(@Path("url") url: String): Call<RemoteCheque>
}