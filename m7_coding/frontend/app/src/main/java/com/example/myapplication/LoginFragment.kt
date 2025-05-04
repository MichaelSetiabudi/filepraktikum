package com.example.myapplication

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.tutorm7front.network.RetrofitInstance
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class LoginFragment : Fragment() {

    private lateinit var etUsername: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnLogin: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        etUsername = view.findViewById(R.id.etUsername)
        etPassword = view.findViewById(R.id.etPassword)
        btnLogin = view.findViewById(R.id.btnLogin)

        btnLogin.setOnClickListener {
            val username = etUsername.text.toString().trim()
            val password = etPassword.text.toString().trim()

            if (validateInputs(username, password)) {
                performLogin(username, password)
            }
        }
    }

    private fun validateInputs(username: String, password: String): Boolean {
        if (username.isEmpty()) {
            etUsername.error = "ID Karyawan tidak boleh kosong"
            return false
        }

        if (password.isEmpty()) {
            etPassword.error = "Password tidak boleh kosong"
            return false
        }

        if (password.length != 6 || !password.all { it.isDigit() }) {
            etPassword.error = "Password harus 6 digit angka (ddMMyy)"
            return false
        }

        return true
    }

    private fun performLogin(employeeId: String, password: String) {
        lifecycleScope.launch {
            try {
                val employees = RetrofitInstance.instance.getEmployee()

                val employee = employees.find { it.id_karyawan == employeeId }

                if (employee != null) {
                    val passwordFromDob = convertDobToPassword(employee.dob_karyawan)

                    if (password == passwordFromDob) {
                        navigateBasedOnRole(employee.status_karyawan, employee.nama_karyawan)
                    } else {
                        showError("Password salah")
                    }
                } else {
                    showError("ID Karyawan tidak ditemukan")
                }

            } catch (e: Exception) {
                Log.e("LoginFragment", "Login error", e)
                showError("Terjadi kesalahan: ${e.message}")
            }
        }
    }

    private fun convertDobToPassword(dob: String): String {
        try {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val outputFormat = SimpleDateFormat("ddMMyy", Locale.getDefault())

            val date = inputFormat.parse(dob)
            return date?.let { outputFormat.format(it) } ?: ""
        } catch (e: Exception) {
            e.printStackTrace()
            return ""
        }
    }

    private fun navigateBasedOnRole(statusKaryawan: String, employeeName: String) {
        val bundle = Bundle().apply {
            putString("employeeName", employeeName)
        }

        when (statusKaryawan) {
            "0" -> {
                findNavController().navigate(R.id.action_loginFragment_to_adminDashboardFragment, bundle)
            }
            "1" -> {
                findNavController().navigate(R.id.action_loginFragment_to_cashierDashboardFragment, bundle)
            }
            else -> {
                showError("Status karyawan tidak valid")
            }
        }
    }

    private fun showError(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}