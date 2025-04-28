package com.example.myapplication

import android.content.Context
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class User(val phone: String, val name: String, val password: String)

class UserData {
    companion object {
        var users = mutableListOf<User>()
        var currentUserPhone = ""
        var currentUserName = ""
        val userFriends = mutableMapOf<String, MutableList<String>>()
        val messages = mutableMapOf<String, MutableList<Message>>()
        private lateinit var database: AppDatabase

        val friends: MutableList<String>
            get() = userFriends[currentUserPhone] ?: mutableListOf()

        fun initialize(context: Context) {
            database = AppDatabase.getDatabase(context)

            CoroutineScope(Dispatchers.IO).launch {
                loadDataFromDatabase()
            }
        }

        private suspend fun loadDataFromDatabase() {
            withContext(Dispatchers.IO) {
                val userEntities = database.userDao().getAllUsers()
                users.clear()
                userEntities.forEach { entity ->
                    users.add(User(entity.phone, entity.name, entity.password))
                }

                userFriends.clear()
                val friendEntities = database.friendDao().getFriendsForUser(currentUserPhone)
                if (friendEntities.isNotEmpty()) {
                    val friendsList = mutableListOf<String>()
                    friendEntities.forEach { entity ->
                        friendsList.add(entity.friendPhone)
                    }
                    userFriends[currentUserPhone] = friendsList
                }

                messages.clear()
                val messageEntities = database.messageDao().getMessagesForUser(currentUserPhone)
                messageEntities.forEach { entity ->
                    if (!messages.containsKey(entity.chatKey)) {
                        messages[entity.chatKey] = mutableListOf()
                    }

                    messages[entity.chatKey]!!.add(
                        Message(
                            entity.sender,
                            entity.receiver,
                            entity.content,
                            entity.timestamp,
                            entity.isRead,
                            entity.isEdited,
                            entity.isUnsent
                        )
                    )
                }
            }
        }

        fun loadUserFriends() {
            if (!userFriends.containsKey(currentUserPhone)) {
                userFriends[currentUserPhone] = mutableListOf()
            }

            for (chatKey in messages.keys) {
                val participants = chatKey.split("-")
                if (participants.size == 2) {
                    if (participants[0] == currentUserPhone && !userFriends[currentUserPhone]!!.contains(participants[1])) {
                        userFriends[currentUserPhone]!!.add(participants[1])
                    }
                    else if (participants[1] == currentUserPhone && !userFriends[currentUserPhone]!!.contains(participants[0])) {
                        userFriends[currentUserPhone]!!.add(participants[0])
                    }
                }
            }

            // Save friends to database
            saveFriends()
        }

        fun addFriend(friendPhone: String) {
            if (!userFriends.containsKey(currentUserPhone)) {
                userFriends[currentUserPhone] = mutableListOf()
            }

            if (!friends.contains(friendPhone)) {
                userFriends[currentUserPhone]!!.add(friendPhone)

                CoroutineScope(Dispatchers.IO).launch {
                    database.friendDao().insertFriend(
                        FriendEntity(
                            userPhone = currentUserPhone,
                            friendPhone = friendPhone
                        )
                    )
                }
            }
        }
        fun saveUser(user: User) {
            CoroutineScope(Dispatchers.IO).launch {
                database.userDao().insertUser(
                    UserEntity(
                        phone = user.phone,
                        name = user.name,
                        password = user.password
                    )
                )
            }
        }

        fun saveAllUsers() {
            CoroutineScope(Dispatchers.IO).launch {
                val userEntities = users.map { user ->
                    UserEntity(
                        phone = user.phone,
                        name = user.name,
                        password = user.password
                    )
                }
                database.userDao().insertUsers(userEntities)
            }
        }

        fun saveFriends() {
            CoroutineScope(Dispatchers.IO).launch {
                val friendEntities = mutableListOf<FriendEntity>()

                userFriends.forEach { (userPhone, friendsList) ->
                    friendsList.forEach { friendPhone ->
                        friendEntities.add(
                            FriendEntity(
                                userPhone = userPhone,
                                friendPhone = friendPhone
                            )
                        )
                    }
                }

                database.friendDao().insertFriends(friendEntities)
            }
        }

        fun saveMessage(chatKey: String, message: Message) {
            CoroutineScope(Dispatchers.IO).launch {
                database.messageDao().insertMessage(
                    MessageEntity(
                        chatKey = chatKey,
                        sender = message.sender,
                        receiver = message.receiver,
                        content = message.content,
                        timestamp = message.timestamp,
                        isRead = message.isRead,
                        isEdited = message.isEdited,
                        isUnsent = message.isUnsent
                    )
                )
            }
        }

        fun saveAllMessages() {
            CoroutineScope(Dispatchers.IO).launch {
                val messageEntities = mutableListOf<MessageEntity>()

                messages.forEach { (chatKey, messageList) ->
                    messageList.forEach { message ->
                        messageEntities.add(
                            MessageEntity(
                                chatKey = chatKey,
                                sender = message.sender,
                                receiver = message.receiver,
                                content = message.content,
                                timestamp = message.timestamp,
                                isRead = message.isRead,
                                isEdited = message.isEdited,
                                isUnsent = message.isUnsent
                            )
                        )
                    }
                }

                database.messageDao().insertMessages(messageEntities)
            }
        }

        fun updateMessage(chatKey: String, message: Message) {
            CoroutineScope(Dispatchers.IO).launch {
                val messages = database.messageDao().getMessagesByChatKey(chatKey)

                // Find the matching message by sender, receiver, content and timestamp
                val messageToUpdate = messages.find {
                    it.sender == message.sender &&
                            it.receiver == message.receiver &&
                            it.timestamp == message.timestamp
                }

                if (messageToUpdate != null) {
                    messageToUpdate.content = message.content
                    messageToUpdate.isRead = message.isRead
                    messageToUpdate.isEdited = message.isEdited
                    messageToUpdate.isUnsent = message.isUnsent

                    database.messageDao().updateMessage(messageToUpdate)
                }
            }
        }

        fun markMessagesAsRead(chatKey: String, userPhone: String) {
            CoroutineScope(Dispatchers.IO).launch {
                database.messageDao().markMessagesAsRead(chatKey, userPhone)
            }
        }
    }
}

data class Message(
    val sender: String,
    val receiver: String,
    var content: String,
    val timestamp: String,
    var isRead: Boolean = false,
    var isEdited: Boolean = false,
    var isUnsent: Boolean = false
)