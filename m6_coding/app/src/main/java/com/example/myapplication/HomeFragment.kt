package com.example.myapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class HomeFragment : Fragment() {
    private lateinit var tvUserInfo: TextView
    private lateinit var btnLogout: Button
    private lateinit var btnAddFriend: ImageView
    private lateinit var rvFriends: RecyclerView
    private lateinit var adapter: FriendAdapter

    private val chatViewModel: ChatViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tvUserInfo = view.findViewById(R.id.tvUserInfo)
        btnLogout = view.findViewById(R.id.btnLogout)
        btnAddFriend = view.findViewById(R.id.btnAddFriend)
        rvFriends = view.findViewById(R.id.rvFriends)

        tvUserInfo.text = "${UserData.currentUserName}\n(${UserData.currentUserPhone})"

        rvFriends.layoutManager = LinearLayoutManager(requireContext())

        adapter = FriendAdapter(this, chatViewModel)
        rvFriends.adapter = adapter

        btnLogout.setOnClickListener {
            UserData.currentUserPhone = ""
            UserData.currentUserName = ""
            findNavController().navigate(R.id.action_homeFragment_to_loginFragment)
        }

        btnAddFriend.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_addFriendFragment)
        }

    }

}