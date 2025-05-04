package com.example.tutorm7front

import com.squareup.moshi.Json
import java.io.Serializable

data class HeroEntity(
    val id: String,
    val name: String,
    val description: String,
    val difficulty: String,
    val image: String,
    @Json(name = "deleted_at") val deletedAt: String?
):Serializable
