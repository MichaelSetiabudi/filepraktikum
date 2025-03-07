package com.example.tugasm1

class StandardUser(
    phoneNumber: String,
    name: String,
    password: String
) : User(phoneNumber, name, password) {

    override val accountType: String = "STANDARD"

    override fun canTakeLoan(): Boolean {
        return loan == 0
    }
}