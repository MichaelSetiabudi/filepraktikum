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

class LoginFragment : Fragment() {

    private lateinit var etPhoneNumber: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnLogin: Button
    private lateinit var btnRegister: Button
    private lateinit var database: AppDatabase

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        database = AppDatabase.getDatabase(requireContext())


        etPhoneNumber = view.findViewById(R.id.etPhoneNumber)
        etPassword = view.findViewById(R.id.etPassword)
        btnLogin = view.findViewById(R.id.btnLogin)
        btnRegister = view.findViewById(R.id.btnRegister)

        btnLogin.setOnClickListener {
            loginUser()
        }

        btnRegister.setOnClickListener {
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

        lifecycleScope.launch {
            val user = withContext(Dispatchers.IO) {
                database.userDao().getUserByPhoneAndPassword(phone, pass)
            }

            if (user != null) {
                UserData.currentUserPhone = phone
                UserData.currentUserName = user.name

                withContext(Dispatchers.IO) {
                    val friendEntities = database.friendDao().getFriendsForUser(phone)
                    val friendsList = mutableListOf<String>()
                    friendEntities.forEach { entity ->
                        friendsList.add(entity.friendPhone)
                    }
                    UserData.userFriends[phone] = friendsList

                    val messageEntities = database.messageDao().getMessagesForUser(phone)
                    messageEntities.forEach { entity ->
                        if (!UserData.messages.containsKey(entity.chatKey)) {
                            UserData.messages[entity.chatKey] = mutableListOf()
                        }

                        UserData.messages[entity.chatKey]!!.add(
                            Message(
                                entity.sender,
                                entity.receiver,
                                entity.content,
                                entity.timestamp,
                                entity.isRead,
                                entity.isEdited,
                                entity.isUnsent
                            )
                        )
                    }
                }

                findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
            } else {
                Toast.makeText(requireContext(), "Password Salah atau Nomor Telepon tidak terdaftar", Toast.LENGTH_SHORT).show()
            }
        }
    }
}