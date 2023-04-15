package com.example.myapplication.data.models

data class RemoteCheque(
    val date: String,
    val totalPrice: Double,
    val items: List<ProductItem>
)