package com.example.myapplication

class User(val phone: String, val name: String, val password: String)

class UserData {
    companion object {
        val users = mutableListOf<User>()
        var currentUserPhone = ""
        var currentUserName = ""
        val userFriends = mutableMapOf<String, MutableList<String>>()

        val friends: MutableList<String>
            get() = userFriends[currentUserPhone] ?: mutableListOf()

        val messages = mutableMapOf<String, MutableList<Message>>()

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
        }

        fun addFriend(friendPhone: String) {
            if (!userFriends.containsKey(currentUserPhone)) {
                userFriends[currentUserPhone] = mutableListOf()
            }

            if (!friends.contains(friendPhone)) {
                userFriends[currentUserPhone]!!.add(friendPhone)
            }
        }
    }
}

class Message(val sender: String, val receiver: String, val content: String, val timestamp: String)