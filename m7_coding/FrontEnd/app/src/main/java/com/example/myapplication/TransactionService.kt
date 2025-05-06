package com.example.myapplication

import retrofit2.http.GET
import retrofit2.http.Path

interface TransactionService {
    @GET("transaction/transaction-report/{nama_karyawan}")
    suspend fun getTransactionReport(@Path("nama_karyawan") employeeName: String): List<TransactionEntity>
    @GET("transaction/transaction-history/{id_karyawan}")
    suspend fun getTransactionHistory(@Path("id_karyawan") employeeId: String): List<TransactionEntity>
}