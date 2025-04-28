package com.example.myapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RegisterFragment : Fragment() {

    private lateinit var etPhoneNumber: EditText
    private lateinit var etFullName: EditText
    private lateinit var etPassword: EditText
    private lateinit var etConfirmPassword: EditText
    private lateinit var btnRegister: Button
    private lateinit var btnLogin: Button
    private lateinit var database: AppDatabase

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_register, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        database = AppDatabase.getDatabase(requireContext())

        etPhoneNumber = view.findViewById(R.id.etPhoneNumber)
        etFullName = view.findViewById(R.id.etFullName)
        etPassword = view.findViewById(R.id.etPassword)
        etConfirmPassword = view.findViewById(R.id.etConfirmPassword)
        btnRegister = view.findViewById(R.id.btnRegister)
        btnLogin = view.findViewById(R.id.btnLoginfromRegister)

        btnRegister.setOnClickListener {
            registerUser()
        }

        btnLogin.setOnClickListener {
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

        lifecycleScope.launch {
            val existingUser = withContext(Dispatchers.IO) {
                database.userDao().getUserByPhone(phone)
            }

            if (existingUser != null) {
                Toast.makeText(requireContext(), "Nomor Telepon telah digunakan", Toast.LENGTH_SHORT).show()
                return@launch
            }
            val newUser = User(phone, name, pass)
            UserData.users.add(newUser)

            withContext(Dispatchers.IO) {
                database.userDao().insertUser(
                    UserEntity(
                        phone = phone,
                        name = name,
                        password = pass
                    )
                )
            }

            Toast.makeText(requireContext(), "Register success", Toast.LENGTH_SHORT).show()

            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }
    }
}