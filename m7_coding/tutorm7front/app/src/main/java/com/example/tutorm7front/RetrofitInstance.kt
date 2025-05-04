package com.example.tutorm7front.network

import com.example.tutorm7front.HeroService
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object RetrofitInstance {
//    Menggunakan ip kalian sendiri bisa cek di ipconfig /all, ip yang diggunakan tiap orang pasti berbeda
//    private const val BASE_URL = "http://192.168.0.144:3000/api/"

    //    Menggunakan emulator dari android studio dan menjalankan backend di device kalian sendiri
    private const val BASE_URL = "http://10.0.2.2:3000/api/"
    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi).asLenient())
            .build()
    }

    val instance: HeroService by lazy {
        retrofit.create(HeroService::class.java)
    }
}
