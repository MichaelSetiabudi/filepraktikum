package com.example.myapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

class AddFriendFragment : Fragment() {
    private lateinit var etSearchPhone: EditText
    private lateinit var btnSearch: Button
    private lateinit var btnAdd: Button
    private lateinit var tvSearchResult: TextView
    private lateinit var btnBack: ImageView

    private var foundUserPhone = ""
    private var foundUserName = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add_friend, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        etSearchPhone = view.findViewById(R.id.etSearchPhone)
        btnSearch = view.findViewById(R.id.btnSearch)
        btnAdd = view.findViewById(R.id.btnAdd)
        tvSearchResult = view.findViewById(R.id.tvSearchResult)
        btnBack = view.findViewById(R.id.btnBack)

        btnSearch.setOnClickListener {
            searchUser()
        }

        btnAdd.setOnClickListener {
            addFriend()
        }

        btnBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun searchUser() {
        val phone = etSearchPhone.text.toString().trim()

        if (phone.isEmpty()) {
            Toast.makeText(requireContext(), "Enter phone number", Toast.LENGTH_SHORT).show()
            return
        }

        if (phone == UserData.currentUserPhone) {
            Toast.makeText(requireContext(), "Cannot add yourself", Toast.LENGTH_SHORT).show()
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
            tvSearchResult.text = "User not found"
            btnAdd.isEnabled = false
            findNavController().navigate(R.id.action_addFriendFragment_to_homeFragment)
        } else {
            tvSearchResult.text = "Found: $foundUserName"
            btnAdd.isEnabled = true
        }
    }

    private fun addFriend() {
        if (foundUserPhone.isEmpty()) {
            return
        }

        if (UserData.friends.contains(foundUserPhone)) {
            Toast.makeText(requireContext(), "Already added as friend", Toast.LENGTH_SHORT).show()
            return
        }

        UserData.addFriend(foundUserPhone)
        Toast.makeText(requireContext(), "Friend added", Toast.LENGTH_SHORT).show()
        findNavController().navigate(R.id.action_addFriendFragment_to_homeFragment)
    }
}