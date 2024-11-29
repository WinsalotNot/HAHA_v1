package com.example.HAHA.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.HAHA.Data.ChatRoomData
import com.example.HAHA.R

class ChatListAdapter(
    private val chatRooms: MutableList<Triple<ChatRoomData, String, String>>, // Triple of ChatRoomData, last message, and name
    private val onItemClick: (ChatRoomData) -> Unit
) : RecyclerView.Adapter<ChatListAdapter.ChatRoomViewHolder>() {

    inner class ChatRoomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val membersNameText: TextView = itemView.findViewById(R.id.CMembersname)
        private val lastMessageText: TextView = itemView.findViewById(R.id.Cmessage)

        // Bind the chat room data, last message, and name to the UI components
        fun bind(chatRoom: ChatRoomData, lastMessage: String, name: String) {
            val membersString = chatRoom.members.joinToString(", ")
            membersNameText.text = name
            lastMessageText.text = lastMessage
            itemView.setOnClickListener { onItemClick(chatRoom) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatRoomViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.chat_card, parent, false)
        return ChatRoomViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChatRoomViewHolder, position: Int) {
        val (chatRoom, lastMessage, name) = chatRooms[position] // Destructure the Triple
        holder.bind(chatRoom, lastMessage, name)
    }

    override fun getItemCount(): Int = chatRooms.size

    // Modify the method to accept and store the additional 'name' data
    fun addChatRoom(chatRoom: ChatRoomData, lastMessage: String, name: String) {
        chatRooms.add(Triple(chatRoom, lastMessage, name)) // Store the data in a Triple
        notifyItemInserted(chatRooms.size - 1)
    }
}
