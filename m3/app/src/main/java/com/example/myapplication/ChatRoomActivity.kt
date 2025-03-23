package com.example.myapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ChatRoomActivity : AppCompatActivity() {
    lateinit var btnBack: ImageView
    lateinit var tvChatHeader: TextView
    lateinit var rvMessages: RecyclerView
    lateinit var etMessage: EditText
    lateinit var btnSend: Button
    lateinit var adapter: MessageAdapter

    var friendPhone = ""
    var friendName = ""
    var chatKey = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_room)

        friendPhone = intent.getStringExtra("friendPhone") ?: ""
        friendName = intent.getStringExtra("friendName") ?: ""

        chatKey = if (UserData.currentUserPhone < friendPhone) {
            "${UserData.currentUserPhone}-$friendPhone"
        } else {
            "$friendPhone-${UserData.currentUserPhone}"
        }

        if (!UserData.messages.containsKey(chatKey)) {
            UserData.messages[chatKey] = mutableListOf()
        }

        btnBack = findViewById(R.id.btnBack)
        tvChatHeader = findViewById(R.id.tvChatHeader)
        rvMessages = findViewById(R.id.rvMessages)
        etMessage = findViewById(R.id.etMessage)
        btnSend = findViewById(R.id.btnSend)

        tvChatHeader.text = friendName

        rvMessages.layoutManager = LinearLayoutManager(this)
        adapter = MessageAdapter()
        rvMessages.adapter = adapter

        // Scroll to the last message if there are any messages
        if ((UserData.messages[chatKey]?.size ?: 0) > 0) {
            rvMessages.scrollToPosition(UserData.messages[chatKey]?.size?.minus(1) ?: 0)
        }

        btnSend.setOnClickListener {
            sendMessage()
        }

        btnBack.setOnClickListener {
            finish()
        }
    }

    fun sendMessage() {
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

    inner class MessageAdapter : RecyclerView.Adapter<MessageAdapter.ViewHolder>() {

        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val tvSender: TextView = view.findViewById(R.id.tvSender)
            val tvContent: TextView = view.findViewById(R.id.tvContent)
            val tvTimestamp: TextView = view.findViewById(R.id.tvTimestamp)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val layout = if (viewType == 0) {
                R.layout.item_message_sent
            } else {
                R.layout.item_message_received
            }

            val view = LayoutInflater.from(parent.context).inflate(layout, parent, false)
            return ViewHolder(view)
        }

        override fun getItemCount(): Int {
            return UserData.messages[chatKey]?.size ?: 0
        }

        override fun getItemViewType(position: Int): Int {
            val message = UserData.messages[chatKey]?.get(position)
            return if (message?.sender == UserData.currentUserPhone) 0 else 1
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val message = UserData.messages[chatKey]?.get(position)

            if (message != null) {
                val senderName = if (message.sender == UserData.currentUserPhone) {
                    UserData.currentUserName
                } else {
                    friendName
                }

                holder.tvSender.text = senderName
                holder.tvContent.text = message.content
                holder.tvTimestamp.text = message.timestamp
            }
        }
    }
}