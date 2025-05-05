package com.example.myapplication
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.POST

interface CustomerService {
    @GET("customers/find/{name}")
    suspend fun findCustomerByName(@Path("name") name: String): Customer?

    @POST("customers")
    suspend fun createCustomer(@Body customer: Customer): Customer
}