package com.example.myproject

data class CartItem(
    val id: Int,
    val name: String,
    val price: Int,
    var quantity: Int = 1
)