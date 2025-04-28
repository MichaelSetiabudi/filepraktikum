package com.example.myapplication

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ChatViewModel(application: Application) : AndroidViewModel(application) {
    private val database = AppDatabase.getDatabase(application)

    // LiveData for all messages in a specific chat
    private val _chatMessages = MutableLiveData<List<Message>>()
    val chatMessages: LiveData<List<Message>> = _chatMessages

    // LiveData for all chat sessions (for home screen)
    private val _allChats = MutableLiveData<Map<String, List<Message>>>()
    val allChats: LiveData<Map<String, List<Message>>> = _allChats

    init {
        // Initialize with existing data from UserData
        _allChats.value = UserData.messages
        loadAllChatsAndEnsureFriends()
    }

    // Load all chats and ensure sender is added as friend
    private fun loadAllChatsAndEnsureFriends() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                // Load all messages from database
                val allMessages = database.messageDao().getAllMessages()

                // Process messages and organize by chat key
                val chatMap = mutableMapOf<String, MutableList<Message>>()

                for (entity in allMessages) {
                    val chatKey = entity.chatKey
                    if (!chatMap.containsKey(chatKey)) {
                        chatMap[chatKey] = mutableListOf()
                    }

                    chatMap[chatKey]?.add(
                        Message(
                            sender = entity.sender,
                            receiver = entity.receiver,
                            content = entity.content,
                            timestamp = entity.timestamp,
                            isRead = entity.isRead,
                            isEdited = entity.isEdited,
                            isUnsent = entity.isUnsent
                        )
                    )

                    // Ensure friend relationship exists when receiving messages
                    if (entity.receiver == UserData.currentUserPhone &&
                        !UserData.friends.contains(entity.sender)) {
                        UserData.addFriend(entity.sender)
                    }
                }

                // Update UserData messages
                UserData.messages.clear()
                UserData.messages.putAll(chatMap)
            }

            // Update LiveData
            _allChats.value = UserData.messages
        }
    }

    // Get messages for a specific chat
    fun loadChatMessages(chatKey: String) {
        viewModelScope.launch {
            val messagesFromDb = withContext(Dispatchers.IO) {
                database.messageDao().getMessagesByChatKey(chatKey)
            }

            // Update UserData from database
            val messages = messagesFromDb.map { entity ->
                Message(
                    sender = entity.sender,
                    receiver = entity.receiver,
                    content = entity.content,
                    timestamp = entity.timestamp,
                    isRead = entity.isRead,
                    isEdited = entity.isEdited,
                    isUnsent = entity.isUnsent
                )
            }

            // Update in-memory data structure
            UserData.messages[chatKey] = messages.toMutableList()

            // Update LiveData
            _chatMessages.value = messages
        }
    }

    // Send a new message
    fun sendMessage(chatKey: String, sender: String, receiver: String, content: String, timestamp: String) {
        val message = Message(sender, receiver, content, timestamp)

        // Update in-memory data structure
        if (!UserData.messages.containsKey(chatKey)) {
            UserData.messages[chatKey] = mutableListOf()
        }
        (UserData.messages[chatKey] as MutableList<Message>).add(message)

        // Save to database
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                database.messageDao().insertMessage(
                    MessageEntity(
                        chatKey = chatKey,
                        sender = sender,
                        receiver = receiver,
                        content = content,
                        timestamp = timestamp,
                        isRead = false,
                        isEdited = false,
                        isUnsent = false
                    )
                )
            }
        }

        // Update LiveData
        _chatMessages.value = UserData.messages[chatKey]
        _allChats.value = UserData.messages
    }

    // Mark messages as read
    fun markMessagesAsRead(chatKey: String, userPhone: String) {
        val messages = UserData.messages[chatKey] ?: return
        var updated = false

        for (message in messages) {
            if (message.receiver == userPhone && !message.isRead) {
                message.isRead = true
                updated = true
            }
        }

        if (updated) {
            // Update database
            viewModelScope.launch {
                withContext(Dispatchers.IO) {
                    database.messageDao().markMessagesAsRead(chatKey, userPhone)
                }
            }

            // Update LiveData
            _chatMessages.value = messages
            _allChats.value = UserData.messages
        }
    }

    fun editMessage(chatKey: String, messagePosition: Int, newContent: String) {
        val messages = UserData.messages[chatKey] ?: return
        if (messagePosition < 0 || messagePosition >= messages.size) return

        // Update the message in memory
        val message = messages[messagePosition]
        message.content = newContent
        message.isEdited = true

        // Update in database
        viewModelScope.launch {
            UserData.updateMessage(chatKey, message)
        }

        // Update LiveData
        _chatMessages.value = messages
        _allChats.value = UserData.messages
    }

    fun unsendMessage(chatKey: String, messagePosition: Int, senderName: String) {
        val messages = UserData.messages[chatKey] ?: return
        if (messagePosition < 0 || messagePosition >= messages.size) return

        // Mark the message as unsent and update the content
        val message = messages[messagePosition]
        message.isUnsent = true
        message.content = "Message has been unsent by $senderName"

        // Update in database
        viewModelScope.launch {
            UserData.updateMessage(chatKey, message)
        }

        // Update LiveData
        _chatMessages.value = messages
        _allChats.value = UserData.messages
    }
}