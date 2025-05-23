package com.example.myapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MessageAdapter(
    private val currentUserPhone: String,
    private val currentUserName: String,
    private val onMessageDoubleClick: (Message, Int) -> Unit,
    private val onMessageLongClick: (Message, Int) -> Boolean
) : RecyclerView.Adapter<MessageAdapter.ViewHolder>() {

    private var messages: List<Message> = listOf()

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvSender: TextView = view.findViewById(R.id.tvSender)
        val tvContent: TextView = view.findViewById(R.id.tvContent)
        val tvTimestamp: TextView = view.findViewById(R.id.tvTimestamp)
        val tvEdited: TextView? = view.findViewById(R.id.tvEdited)
        val ivReadStatus: ImageView? = view.findViewById(R.id.ivReadStatus)
        val cardView: View = view.findViewById(R.id.cvMessage)
    }

    fun updateMessages(newMessages: List<Message>) {
        this.messages = newMessages
        notifyDataSetChanged()
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
        return messages.size
    }

    override fun getItemViewType(position: Int): Int {
        val message = messages[position]
        return if (message.sender == currentUserPhone) 0 else 1
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val message = messages[position]

        if (message.sender == currentUserPhone) {
            holder.tvSender.text = "You"
        } else {
            var senderName = "Unknown"
            for (user in UserData.users) {
                if (user.phone == message.sender) {
                    senderName = user.name
                    break
                }
            }
            holder.tvSender.text = senderName
        }

        holder.tvContent.text = message.content

        if (message.isUnsent) {
            holder.tvContent.setTextColor(holder.tvContent.context.getColor(android.R.color.darker_gray))
            holder.tvContent.setTypeface(null, android.graphics.Typeface.ITALIC)

            holder.ivReadStatus?.visibility = View.GONE
        } else {
            holder.tvContent.setTextColor(holder.tvContent.context.getColor(android.R.color.black))
            holder.tvContent.setTypeface(null, android.graphics.Typeface.NORMAL)

            if (message.sender == currentUserPhone && holder.ivReadStatus != null) {
                holder.ivReadStatus.visibility = View.VISIBLE
                if (message.isRead) {
                    holder.ivReadStatus.setImageResource(R.drawable.ic_double_check)
                } else {
                    holder.ivReadStatus.setImageResource(R.drawable.ic_single_check)
                }
            } else if (holder.ivReadStatus != null) {
                holder.ivReadStatus.visibility = View.GONE
            }
        }

        holder.tvTimestamp.text = message.timestamp

        holder.tvEdited?.visibility = if (message.isEdited && !message.isUnsent) View.VISIBLE else View.GONE

        if (message.sender == currentUserPhone) {
            var lastClickTime: Long = 0

            holder.cardView.setOnClickListener {
                val clickTime = System.currentTimeMillis()
                if (clickTime - lastClickTime < 500 && !message.isUnsent) {
                    onMessageDoubleClick(message, position)
                }
                lastClickTime = clickTime
            }

            holder.cardView.setOnLongClickListener {
                if (!message.isUnsent) {
                    onMessageLongClick(message, position)
                } else {
                    false
                }
            }
        }
    }
}