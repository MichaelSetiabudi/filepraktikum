package com.example.myapplication

import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path

interface ProductService {
    @GET("product")
    suspend fun getAllProduct(): List<ProductEntity>
}
