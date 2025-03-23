package com.example.myapplication

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class RegisterActivity : AppCompatActivity() {
    lateinit var etPhoneNumber: EditText
    lateinit var etFullName: EditText
    lateinit var etPassword: EditText
    lateinit var etConfirmPassword: EditText
    lateinit var btnRegister: Button
    lateinit var btnLogin:Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        etPhoneNumber = findViewById(R.id.etPhoneNumber)
        etFullName = findViewById(R.id.etFullName)
        etPassword = findViewById(R.id.etPassword)
        etConfirmPassword = findViewById(R.id.etConfirmPassword)
        btnRegister = findViewById(R.id.btnRegister)
        btnLogin=findViewById(R.id.btnLoginfromRegister)
        btnRegister.setOnClickListener {
            registerUser()
        }
        btnLogin.setOnClickListener {
            finish()
        }
    }

    fun registerUser() {
        val phone = etPhoneNumber.text.toString().trim()
        val name = etFullName.text.toString().trim()
        val pass = etPassword.text.toString().trim()
        val confirmPass = etConfirmPassword.text.toString().trim()

        if (phone.isEmpty() || name.isEmpty() || pass.isEmpty() || confirmPass.isEmpty()) {
            Toast.makeText(this, "Fill all fields", Toast.LENGTH_SHORT).show()
            return
        }

        if (pass != confirmPass) {
            Toast.makeText(this, "Passwords don't match", Toast.LENGTH_SHORT).show()
            return
        }

        var phoneExists = false
        for (i in 0 until UserData.users.size) {
            if (UserData.users[i].phone == phone) {
                phoneExists = true
                break
            }
        }

        if (phoneExists) {
            Toast.makeText(this, "Phone already used", Toast.LENGTH_SHORT).show()
            return
        }

        val newUser = User(phone, name, pass)
        UserData.users.add(newUser)

        Toast.makeText(this, "Register success", Toast.LENGTH_SHORT).show()
        finish()
    }
}