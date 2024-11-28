package com.example.HAHA.Adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.HAHA.Data.ChatData
import com.example.HAHA.R

class ChatAdapter(private val messages: MutableList<ChatData>, private val userId: String) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun getItemViewType(position: Int): Int {
        return if (messages[position].senderId == userId) {
            R.layout.sender_chat_message
        } else {
            R.layout.receiver_chat_message
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
        return ChatViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = messages[position]
        (holder as ChatViewHolder).bind(message)
    }

    override fun getItemCount(): Int = messages.size

    fun addMessage(chatMessage: ChatData) {
        messages.add(chatMessage)
        notifyItemInserted(messages.size - 1)
    }

    inner class ChatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val messageTextView: TextView = itemView.findViewById(R.id.messageText)
        private val nameTextView: TextView = itemView.findViewById(R.id.nameText)

        fun bind(chatMessage: ChatData) {
            messageTextView.text = chatMessage.message
            Log.d("ChatAdapter", "SenderId: ${chatMessage.senderId}, ReceiverId: ${chatMessage.receiverId}, UserId: $userId")
            if (chatMessage.senderId == userId){
                nameTextView.text = chatMessage.receiverName
            }else{
                nameTextView.text = chatMessage.senderName
            }
        }
    }
}

