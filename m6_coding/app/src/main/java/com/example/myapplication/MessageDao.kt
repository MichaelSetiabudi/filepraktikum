package com.example.myapplication

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface MessageDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMessage(message: MessageEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMessages(messages: List<MessageEntity>)

    @Update
    fun updateMessage(message: MessageEntity)

    @Query("SELECT * FROM messages WHERE chatKey = :chatKey ORDER BY id ASC")
    fun getMessagesByChatKey(chatKey: String): List<MessageEntity>

    @Query("UPDATE messages SET isRead = 1 WHERE chatKey = :chatKey AND receiver = :userPhone AND isRead = 0")
    fun markMessagesAsRead(chatKey: String, userPhone: String)

    @Query("SELECT * FROM messages WHERE sender = :userPhone OR receiver = :userPhone")
    fun getMessagesForUser(userPhone: String): List<MessageEntity>
    @Query("SELECT * FROM messages ORDER BY id ASC")
    fun getAllMessages(): List<MessageEntity>

}