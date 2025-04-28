package com.example.myapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
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

    private val chatViewModel: ChatViewModel by viewModels()

    private var friendPhone = ""
    private var friendName = ""
    private var chatKey = ""

    private var isEditing = false
    private var editingMessagePosition = -1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_chat_room, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        friendPhone = arguments?.getString("friendPhone") ?: ""
        friendName = arguments?.getString("friendName") ?: ""

        chatKey = if (UserData.currentUserPhone < friendPhone) {
            "${UserData.currentUserPhone}-$friendPhone"
        } else {
            "$friendPhone-${UserData.currentUserPhone}"
        }

        btnBack = view.findViewById(R.id.btnBack)
        tvChatHeader = view.findViewById(R.id.tvChatHeader)
        rvMessages = view.findViewById(R.id.rvMessages)
        etMessage = view.findViewById(R.id.etMessage)
        btnSend = view.findViewById(R.id.btnSend)

        tvChatHeader.text = friendName

        rvMessages.layoutManager = LinearLayoutManager(requireContext())
        adapter = MessageAdapter(
            UserData.currentUserPhone,
            UserData.currentUserName,
            onMessageDoubleClick = { message, position ->
                if (!message.isUnsent) {
                    startEditingMessage(message, position)
                }
            },
            onMessageLongClick = { message, position ->
                if (message.sender == UserData.currentUserPhone && !message.isUnsent) {
                    showUnsendConfirmationDialog(position)
                    true
                } else {
                    false
                }
            }
        )
        rvMessages.adapter = adapter

        chatViewModel.loadChatMessages(chatKey)
        chatViewModel.chatMessages.observe(viewLifecycleOwner, Observer { messages ->
            adapter.updateMessages(messages)

            if (messages.isNotEmpty()) {
                rvMessages.scrollToPosition(messages.size - 1)
            }

            chatViewModel.markMessagesAsRead(chatKey, UserData.currentUserPhone)
        })
        btnSend.setOnClickListener {
            if (isEditing) {
                updateMessage()
            } else {
                sendMessage()
            }
        }

        btnBack.setOnClickListener {
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

        chatViewModel.sendMessage(
            chatKey,
            UserData.currentUserPhone,
            friendPhone,
            content,
            timestamp
        )

        etMessage.setText("")
    }

    private fun startEditingMessage(message: Message, position: Int) {
        if (message.sender != UserData.currentUserPhone) return

        isEditing = true
        editingMessagePosition = position

        btnSend.text = "Update"

        etMessage.setText(message.content)
        etMessage.setSelection(message.content.length)
        etMessage.requestFocus()
    }

    private fun updateMessage() {
        val newContent = etMessage.text.toString().trim()

        if (newContent.isEmpty() || editingMessagePosition == -1) {
            return
        }   
        chatViewModel.editMessage(chatKey, editingMessagePosition, newContent)
        isEditing = false
        editingMessagePosition = -1
        btnSend.text = "Send"
        etMessage.setText("")
    }

    private fun showUnsendConfirmationDialog(messagePosition: Int) {
        AlertDialog.Builder(requireContext())
            .setTitle("Unsend Message")
            .setMessage("Are you sure you want to unsend this message?")
            .setPositiveButton("Unsend") { dialog, _ ->
                chatViewModel.unsendMessage(chatKey, messagePosition, UserData.currentUserName)
                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }
}