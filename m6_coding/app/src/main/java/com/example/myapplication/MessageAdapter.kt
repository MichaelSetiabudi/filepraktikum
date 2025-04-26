package com.example.myapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MessageAdapter(
    private val chatKey: String,
    private val currentUserPhone: String,
    private val currentUserName: String,
    private val friendName: String
) : RecyclerView.Adapter<MessageAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
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
        return if (message?.sender == currentUserPhone) 0 else 1
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val message = UserData.messages[chatKey]?.get(position)

        if (message != null) {
            val senderName = if (message.sender == currentUserPhone) {
                currentUserName
            } else {
                friendName
            }

            holder.tvSender.text = senderName
            holder.tvContent.text = message.content
            holder.tvTimestamp.text = message.timestamp
        }
    }
}