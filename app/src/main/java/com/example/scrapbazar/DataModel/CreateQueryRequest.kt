package com.example.scrapbazar.DataModel

import java.util.ArrayList

data class CreateQueryRequest(

    var c_id:Int,
    var customer_name:String,
    var mobile:String,
    var product_name: ArrayList<String>?,
    var product_volume:String,
    var Address:String,
    var next_sunday:String,
    var comment:String
)
