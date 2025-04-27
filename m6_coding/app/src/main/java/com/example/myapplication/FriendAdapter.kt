package com.example.myapplication

import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView

class FriendAdapter(
    private val fragment: Fragment,
    private val viewModel: ChatViewModel
) : RecyclerView.Adapter<FriendAdapter.ViewHolder>() {

    private var chats: Map<String, List<Message>> = mapOf()

    init {
        // Observe chat updates
        viewModel.allChats.observe(fragment.viewLifecycleOwner) { updatedChats ->
            chats = updatedChats
            notifyDataSetChanged()
        }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivProfile: ImageView = view.findViewById(R.id.ivProfile)
        val tvName: TextView = view.findViewById(R.id.tvName)
        val tvLastMessage: TextView = view.findViewById(R.id.tvLastMessage)
        val tvTimestamp: TextView = view.findViewById(R.id.tvTimestamp)
        val ivUnreadIndicator: ImageView = view.findViewById(R.id.ivUnreadIndicator) // Add this to your item_friend.xml
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

        val messages = chats[chatKey]

        if (messages == null || messages.isEmpty()) {
            holder.tvLastMessage.text = "No messages yet"
            holder.tvLastMessage.setTypeface(null, Typeface.BOLD_ITALIC)
            holder.tvTimestamp.text = ""
            holder.ivUnreadIndicator.visibility = View.GONE
        } else {
            val lastMsg = messages.last()
            holder.tvLastMessage.text = lastMsg.content
            holder.tvTimestamp.text = lastMsg.timestamp

            // Check if there are unread messages
            val hasUnreadMessages = messages.any {
                it.sender == friendPhone && !it.isRead
            }

            if (hasUnreadMessages) {
                holder.tvLastMessage.setTypeface(null, Typeface.BOLD) // Bold
                holder.ivUnreadIndicator.visibility = View.VISIBLE
            } else {
                holder.tvLastMessage.setTypeface(null, Typeface.NORMAL) // Normal
                holder.ivUnreadIndicator.visibility = View.GONE
            }
        }

        holder.itemView.setOnClickListener {
            val bundle = androidx.core.os.bundleOf(
                "friendPhone" to friendPhone,
                "friendName" to friendName
            )
            fragment.findNavController().navigate(R.id.action_homeFragment_to_chatRoomFragment, bundle)
        }
    }
}