package com.example.scrapbazar.DataModel

data class UserResponseSubListItem(
    val address: String,
    val address_id: Int,
    val area: String,
    val city: String,
    val customer_id: Int,
    val first_name: String,
    val landmark: String,
    val last_name: String,
    val lat: Double,
    val long: Double,
    val mobile: Any,
    val pincode: Int,
    val state: String
)