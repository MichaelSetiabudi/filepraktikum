package com.example.myapplication

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUser(user: UserEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUsers(users: List<UserEntity>)

    @Update
    fun updateUser(user: UserEntity)

    @Query("SELECT * FROM users")
    fun getAllUsers(): List<UserEntity>

    @Query("SELECT * FROM users WHERE phone = :phone")
    fun getUserByPhone(phone: String): UserEntity?

    @Query("SELECT * FROM users WHERE phone = :phone AND password = :password")
    fun getUserByPhoneAndPassword(phone: String, password: String): UserEntity?
}