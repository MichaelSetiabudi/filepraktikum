package com.example.myapplication

import com.squareup.moshi.Json
import java.io.Serializable

data class TransactionEntity(
    val transaction_id: String,
    val customer_name: String,
    val cashier_name: String,
    val total: Int,
    val detail: List<String>
):Serializable