package com.example.myapplication

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "messages")
data class MessageEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val chatKey: String,
    val sender: String,
    val receiver: String,
    var content: String,
    val timestamp: String,
    var isRead: Boolean = false,
    var isEdited: Boolean = false,
    var isUnsent: Boolean = false
)