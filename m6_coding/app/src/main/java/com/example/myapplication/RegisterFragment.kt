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

class RegisterFragment : Fragment() {

    private lateinit var etPhoneNumber: EditText
    private lateinit var etFullName: EditText
    private lateinit var etPassword: EditText
    private lateinit var etConfirmPassword: EditText
    private lateinit var btnRegister: Button
    private lateinit var btnLogin: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate layout untuk fragment ini
        return inflater.inflate(R.layout.fragment_register, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inisialisasi view
        etPhoneNumber = view.findViewById(R.id.etPhoneNumber)
        etFullName = view.findViewById(R.id.etFullName)
        etPassword = view.findViewById(R.id.etPassword)
        etConfirmPassword = view.findViewById(R.id.etConfirmPassword)
        btnRegister = view.findViewById(R.id.btnRegister)
        btnLogin = view.findViewById(R.id.btnLoginfromRegister)

        // Setup click listeners
        btnRegister.setOnClickListener {
            registerUser()
        }

        btnLogin.setOnClickListener {
            // Kembali ke LoginFragment
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }
    }

    private fun registerUser() {
        val phone = etPhoneNumber.text.toString().trim()
        val name = etFullName.text.toString().trim()
        val pass = etPassword.text.toString().trim()
        val confirmPass = etConfirmPassword.text.toString().trim()

        if (phone.isEmpty() || name.isEmpty() || pass.isEmpty() || confirmPass.isEmpty()) {
            Toast.makeText(requireContext(), "Semua field harus diisi!", Toast.LENGTH_SHORT).show()
            return
        }

        if (pass != confirmPass) {
            Toast.makeText(requireContext(), "Password tidak sama", Toast.LENGTH_SHORT).show()
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
            Toast.makeText(requireContext(), "Nomor Telepon telah digunakan", Toast.LENGTH_SHORT).show()
            return
        }

        val newUser = User(phone, name, pass)
        UserData.users.add(newUser)

        Toast.makeText(requireContext(), "Register success", Toast.LENGTH_SHORT).show()

        // Kembali ke halaman login
        findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
    }
}