package com.example.tugasm1

class PlusUser(
    phoneNumber: String,
    name: String,
    password: String
) : User(phoneNumber, name, password) {

    override val accountType: String = "PLUS"

    override fun canTakeLoan(): Boolean {
        return true
    }
}