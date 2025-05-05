package com.example.myapplication

import com.squareup.moshi.Json
import java.io.Serializable

data class ProductEntity(
    val id_produk: String,
    val nama_produk: String,
    val id_kategori: String,
    val stok: Int,
    val harga: Int,
    @Json(name = "deleted_at") val deletedAt: String?
):Serializable