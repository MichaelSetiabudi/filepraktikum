package com.example.myapplication

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ChatViewModel : ViewModel() {
    // LiveData for all messages in a specific chat
    private val _chatMessages = MutableLiveData<List<Message>>()
    val chatMessages: LiveData<List<Message>> = _chatMessages

    // LiveData for all chat sessions (for home screen)
    private val _allChats = MutableLiveData<Map<String, List<Message>>>()
    val allChats: LiveData<Map<String, List<Message>>> = _allChats

    init {
        // Initialize with existing data from UserData
        _allChats.value = UserData.messages
    }

    // Get messages for a specific chat
    fun loadChatMessages(chatKey: String) {
        _chatMessages.value = UserData.messages[chatKey] ?: mutableListOf()
    }

    // Send a new message
    fun sendMessage(chatKey: String, sender: String, receiver: String, content: String, timestamp: String) {
        val message = Message(sender, receiver, content, timestamp)

        // Update local storage
        if (!UserData.messages.containsKey(chatKey)) {
            UserData.messages[chatKey] = mutableListOf()
        }

        (UserData.messages[chatKey] as MutableList<Message>).add(message)

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
            // Update LiveData
            _chatMessages.value = messages
            _allChats.value = UserData.messages
        }
    }
    fun editMessage(chatKey: String, messagePosition: Int, newContent: String) {
        val messages = UserData.messages[chatKey] ?: return
        if (messagePosition < 0 || messagePosition >= messages.size) return

        // Update the message
        val message = messages[messagePosition]
        message.content = newContent
        message.isEdited = true

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

        // Update LiveData
        _chatMessages.value = messages
        _allChats.value = UserData.messages
    }
}