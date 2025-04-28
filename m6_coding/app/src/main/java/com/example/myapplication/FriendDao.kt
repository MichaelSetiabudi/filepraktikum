package com.example.myapplication

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface FriendDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFriend(friend: FriendEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFriends(friends: List<FriendEntity>)

    @Query("SELECT * FROM friends WHERE userPhone = :userPhone")
    fun getFriendsForUser(userPhone: String): List<FriendEntity>

    @Query("SELECT * FROM friends WHERE userPhone = :userPhone AND friendPhone = :friendPhone")
    fun getFriendship(userPhone: String, friendPhone: String): FriendEntity?

    @Query("DELETE FROM friends WHERE userPhone = :userPhone AND friendPhone = :friendPhone")
    fun deleteFriend(userPhone: String, friendPhone: String)
}