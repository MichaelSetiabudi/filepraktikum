package com.example.tutorm7front.network

import com.example.myapplication.CustomerService
import com.example.myapplication.EmployeeService
import com.example.myapplication.ProductService
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object RetrofitInstance {
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

    val product: ProductService by lazy {
        retrofit.create(ProductService::class.java)
    }

    val employee: EmployeeService by lazy {
        retrofit.create(EmployeeService::class.java)
    }

    val customer: CustomerService by lazy {
        retrofit.create(CustomerService::class.java)
    }


}