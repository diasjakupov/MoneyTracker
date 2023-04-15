package com.example.myapplication.data.network

sealed class NetResponse<T>() {
    class Successful<T>(val data: T): NetResponse<T>()
    class Loading<T>(): NetResponse<T>()
    class Error<T>(val message: String): NetResponse<T>()
}