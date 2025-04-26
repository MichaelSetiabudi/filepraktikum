package com.example.myapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

class LoginFragment : Fragment() {

    private lateinit var etPhoneNumber: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnLogin: Button
    private lateinit var btnRegister: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate layout untuk fragment ini
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inisialisasi view
        etPhoneNumber = view.findViewById(R.id.etPhoneNumber)
        etPassword = view.findViewById(R.id.etPassword)
        btnLogin = view.findViewById(R.id.btnLogin)
        btnRegister = view.findViewById(R.id.btnRegister)

        // Setup click listeners
        btnLogin.setOnClickListener {
            loginUser()
        }

        btnRegister.setOnClickListener {
            // Navigasi ke RegisterFragment menggunakan Navigation Component
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }
    }

    private fun loginUser() {
        val phone = etPhoneNumber.text.toString().trim()
        val pass = etPassword.text.toString().trim()

        if (phone.isEmpty() || pass.isEmpty()) {
            Toast.makeText(requireContext(), "Semua harus diisi!", Toast.LENGTH_SHORT).show()
            return
        }

        for (i in 0 until UserData.users.size) {
            if (UserData.users[i].phone == phone && UserData.users[i].password == pass) {
                UserData.currentUserPhone = phone
                UserData.currentUserName = UserData.users[i].name
                UserData.loadUserFriends()

                // Navigate to HomeFragment
                findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
                return
            }
        }

        Toast.makeText(requireContext(), "Password Salah atau Nomor Telepon tidak terdaftar", Toast.LENGTH_SHORT).show()
    }
}