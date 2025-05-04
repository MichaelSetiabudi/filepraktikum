package com.example.tutorm7front


data class HeroResponse(
    val status: Int,
    val heroes: List<HeroEntity>,
    val count: Int
)
