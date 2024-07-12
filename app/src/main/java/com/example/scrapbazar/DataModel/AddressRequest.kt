package com.example.scrapbazar.DataModel

data class AddressRequest(

    val c_id: Int,
    val pincode: String,
    val state: String,
    val city: String,
    val first_name: String,
    val last_name: String,
    val landmark:String,
    val address:String,
    val area:String,
    val lat:String,
    val long:String
)

