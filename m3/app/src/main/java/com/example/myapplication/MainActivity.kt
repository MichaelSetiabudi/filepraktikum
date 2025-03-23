
package com.example.myapplication

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    lateinit var etPhoneNumber: EditText
    lateinit var etPassword: EditText
    lateinit var btnLogin: Button
    lateinit var btnRegister: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        etPhoneNumber = findViewById(R.id.etPhoneNumber)
        etPassword = findViewById(R.id.etPassword)
        btnLogin = findViewById(R.id.btnLogin)
        btnRegister = findViewById(R.id.btnRegister)

        btnLogin.setOnClickListener {
            loginUser()
        }

        btnRegister.setOnClickListener {
            val i = Intent(this, RegisterActivity::class.java)
            startActivity(i)
        }
    }

    fun loginUser() {
        val phone = etPhoneNumber.text.toString().trim()
        val pass = etPassword.text.toString().trim()

        if (phone.isEmpty() || pass.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            return
        }

        for (i in 0 until UserData.users.size) {
            if (UserData.users[i].phone == phone && UserData.users[i].password == pass) {
                UserData.currentUserPhone = phone
                UserData.currentUserName = UserData.users[i].name
                UserData.loadUserFriends() // Add this line to load user's friends
                val intent = Intent(this, HomeActivity::class.java)
                startActivity(intent)
                finish()
                return
            }
        }

        Toast.makeText(this, "Wrong phone or password", Toast.LENGTH_SHORT).show()
    }
}