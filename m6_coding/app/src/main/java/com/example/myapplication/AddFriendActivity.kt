package com.example.myapplication

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class AddFriendActivity : AppCompatActivity() {
    lateinit var etSearchPhone: EditText
    lateinit var btnSearch: Button
    lateinit var btnAdd: Button
    lateinit var tvSearchResult: TextView

    var foundUserPhone = ""
    var foundUserName = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_friend)

        etSearchPhone = findViewById(R.id.etSearchPhone)
        btnSearch = findViewById(R.id.btnSearch)
        btnAdd = findViewById(R.id.btnAdd)
        tvSearchResult = findViewById(R.id.tvSearchResult)

        btnSearch.setOnClickListener {
            searchUser()
        }

        btnAdd.setOnClickListener {
            addFriend()
        }
    }

    fun searchUser() {
        val phone = etSearchPhone.text.toString().trim()

        if (phone.isEmpty()) {
            Toast.makeText(this, "Enter phone number", Toast.LENGTH_SHORT).show()
            return
        }

        if (phone == UserData.currentUserPhone) {
            Toast.makeText(this, "Cannot add yourself", Toast.LENGTH_SHORT).show()
            return
        }

        foundUserPhone = ""
        foundUserName = ""

        for (i in 0 until UserData.users.size) {
            if (UserData.users[i].phone == phone) {
                foundUserPhone = phone
                foundUserName = UserData.users[i].name
                break
            }
        }

        if (foundUserPhone.isEmpty()) {
            tvSearchResult.setText("User not found")
            btnAdd.isEnabled = false
        } else {
            tvSearchResult.setText("Found: $foundUserName")
            btnAdd.isEnabled = true
        }
    }

    fun addFriend() {
        if (foundUserPhone.isEmpty()) {
            return
        }

        if (UserData.friends.contains(foundUserPhone)) {
            Toast.makeText(this, "Already added as friend", Toast.LENGTH_SHORT).show()
            return
        }

        UserData.addFriend(foundUserPhone)
        Toast.makeText(this, "Friend added", Toast.LENGTH_SHORT).show()
        finish()
    }
}