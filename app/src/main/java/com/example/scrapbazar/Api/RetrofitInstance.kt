package com.example.scrapbazar.Api

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


//Object class of Retrofit
object RetrofitInstance {

    private val gson = GsonBuilder()
        .setLenient()
        .create()


    private val retrofit by lazy{
        Retrofit.Builder().baseUrl("https://rndtd.com/demos/scrapbazar/api/")//BasePoint
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

    }

    val apiInterface by lazy{
        retrofit.create(ApiInterface::class.java)
    }

}