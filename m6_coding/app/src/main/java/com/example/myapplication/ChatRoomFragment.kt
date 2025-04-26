package com.example.myapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ChatRoomFragment : Fragment() {
    private lateinit var btnBack: Button
    private lateinit var tvChatHeader: TextView
    private lateinit var rvMessages: RecyclerView
    private lateinit var etMessage: EditText
    private lateinit var btnSend: Button
    private lateinit var adapter: MessageAdapter

    private var friendPhone = ""
    private var friendName = ""
    private var chatKey = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_chat_room, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Get arguments passed from FriendAdapter
        friendPhone = arguments?.getString("friendPhone") ?: ""
        friendName = arguments?.getString("friendName") ?: ""

        chatKey = if (UserData.currentUserPhone < friendPhone) {
            "${UserData.currentUserPhone}-$friendPhone"
        } else {
            "$friendPhone-${UserData.currentUserPhone}"
        }

        if (!UserData.messages.containsKey(chatKey)) {
            UserData.messages[chatKey] = mutableListOf()
        }

        // Initialize views
        btnBack = view.findViewById(R.id.btnBack)
        tvChatHeader = view.findViewById(R.id.tvChatHeader)
        rvMessages = view.findViewById(R.id.rvMessages)
        etMessage = view.findViewById(R.id.etMessage)
        btnSend = view.findViewById(R.id.btnSend)

        tvChatHeader.text = friendName

        // Set up the RecyclerView
        rvMessages.layoutManager = LinearLayoutManager(requireContext())
        adapter = MessageAdapter(
            chatKey,
            UserData.currentUserPhone,
            UserData.currentUserName,
            friendName
        )
        rvMessages.adapter = adapter

        // Scroll to the last message if there are any
        if ((UserData.messages[chatKey]?.size ?: 0) > 0) {
            rvMessages.scrollToPosition(UserData.messages[chatKey]?.size?.minus(1) ?: 0)
        }

        // Set button click listeners
        btnSend.setOnClickListener {
            sendMessage()
        }

        btnBack.setOnClickListener {
            // Navigate back to HomeFragment
            findNavController().navigateUp()
        }
    }

    private fun sendMessage() {
        val content = etMessage.text.toString().trim()

        if (content.isEmpty()) {
            return
        }

        val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        val timestamp = timeFormat.format(Date())

        val message = Message(
            UserData.currentUserPhone,
            friendPhone,
            content,
            timestamp
        )

        UserData.messages[chatKey]?.add(message)

        adapter.notifyDataSetChanged()
        rvMessages.scrollToPosition(UserData.messages[chatKey]?.size?.minus(1) ?: 0)

        etMessage.setText("")
    }
}