package com.example.myapplication

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R

class FriendAdapter(private val fragment: Fragment) : RecyclerView.Adapter<FriendAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivProfile: ImageView = view.findViewById(R.id.ivProfile)
        val tvName: TextView = view.findViewById(R.id.tvName)
        val tvLastMessage: TextView = view.findViewById(R.id.tvLastMessage)
        val tvTimestamp: TextView = view.findViewById(R.id.tvTimestamp)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_friend, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return UserData.friends.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val friendPhone = UserData.friends[position]

        var friendName = ""
        for (i in 0 until UserData.users.size) {
            if (UserData.users[i].phone == friendPhone) {
                friendName = UserData.users[i].name
                break
            }
        }

        holder.tvName.text = friendName

        val chatKey = if (UserData.currentUserPhone < friendPhone) {
            "${UserData.currentUserPhone}-$friendPhone"
        } else {
            "$friendPhone-${UserData.currentUserPhone}"
        }
        val messages = UserData.messages[chatKey]
        if (messages == null || messages.isEmpty()) {
            holder.tvLastMessage.text = "No messages yet"
            holder.tvLastMessage.setTypeface(null, 0)
            holder.tvTimestamp.text = ""
        } else {
            val lastMsg = messages.last()
            holder.tvLastMessage.text = lastMsg.content

            if (lastMsg.sender != UserData.currentUserPhone) {
                holder.tvLastMessage.setTypeface(null, 1)
            } else {
                holder.tvLastMessage.setTypeface(null, 0)
            }

            holder.tvTimestamp.text = lastMsg.timestamp
        }

        holder.itemView.setOnClickListener {
            // Using Navigation Component instead of Intent
            val bundle = androidx.core.os.bundleOf(
                "friendPhone" to friendPhone,
                "friendName" to friendName
            )
            fragment.findNavController().navigate(R.id.action_homeFragment_to_chatRoomFragment, bundle)
        }
    }
}