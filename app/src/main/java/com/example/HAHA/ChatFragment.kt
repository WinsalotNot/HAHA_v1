package com.example.HAHA

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.HAHA.Adapter.ChatAdapter
import com.example.HAHA.Data.ChatData
import com.example.HAHA.Data.ChatRoomData
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ChatFragment : Fragment() {

    private lateinit var sendBtn: Button
    private lateinit var messageInput: EditText
    private lateinit var messagesRecyclerView: RecyclerView
    private lateinit var chatAdapter: ChatAdapter
    private var roomId: String? = null

    val chatDatabase = FirebaseDatabase.getInstance("https://testing-6341f-default-rtdb.asia-southeast1.firebasedatabase.app")
    val chatRoomsRef = chatDatabase.getReference("chatRooms")

    private val TAG = "ChatFragment"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chat, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sharedPreferences: SharedPreferences = requireContext().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)

        messagesRecyclerView = view.findViewById(R.id.chatRecyclerView)

        val userId = sharedPreferences.getString("USER_ID", "900")
        val userName = sharedPreferences.getString("USER_NAME", null)
        val creatorId = arguments?.getInt("creatorId", 66).toString()
        val creatorName = arguments?.getString("name") ?: ""

        // Initialize the EditText and Button
        messageInput = view.findViewById(R.id.messageEditText)
        sendBtn = view.findViewById(R.id.sendButton)

        messagesRecyclerView.layoutManager = LinearLayoutManager(context)

        if (userId != null) {
            getOrCreateChatRoom(userId, creatorId) { id ->
                roomId = id
                loadMessages(roomId!!, userId)
            }
        } else {
            Log.e(TAG, "User ID is null")
        }

        sendBtn.setOnClickListener {
            val messageText = messageInput.text.toString().trim()

            if (roomId != null && messageText.isNotEmpty()) {
                if (userId != null) {
                    if (userName != null) {
                        sendMessage(roomId!!, userId, creatorId, userName, creatorName, messageText)
                    } else {
                        Log.e(TAG, "Username is null")
                    }
                } else {
                    Log.e(TAG, "User ID is null")
                }
                messageInput.text.clear()
            }
        }

    }
    private fun getOrCreateChatRoom(user1: String, user2: String, callback: (roomId: String) -> Unit) {
        // Create a composite key based on user1 and user2
        val chatRoomKey = listOf(user1, user2).sorted().joinToString("_")
        // Query chatRooms by composite key
        val chatRoomRef = chatRoomsRef.child(chatRoomKey)

        chatRoomRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    // If the room exists, return the roomId
                    val existingRoom = snapshot.getValue(ChatRoomData::class.java)
                    callback(existingRoom?.roomId ?: "")
                } else {
                    // Create a new chat room if it doesn't exist
                    val chatRoom = ChatRoomData(
                        roomId = chatRoomKey,
                        members = listOf(user1, user2)
                    )
                    chatRoomRef.setValue(chatRoom)
                        .addOnSuccessListener {
                            callback(chatRoomKey)
                        }
                        .addOnFailureListener { e ->
                            Log.e("ChatRoom", "Error creating chat room", e)
                        }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("ChatRoom", "Database error: ${error.message}")
            }
        })
    }

    private fun sendMessage(roomId: String, senderId: String, receiverId: String, senderName: String, receiverName: String, messageText: String) {
        val messageRef = chatRoomsRef.child(roomId).child("messages").push()
        val chatMessage = ChatData(
            senderId = senderId,
            receiverId = receiverId,
            senderName = senderName,
            receiverName = receiverName,
            message = messageText,
            timestamp = System.currentTimeMillis()
        )

        messageRef.setValue(chatMessage)
            .addOnSuccessListener {
                Log.d("ChatFragment", "Message sent: ${chatMessage.message}")
            }
            .addOnFailureListener { e ->
                Log.e("ChatFragment", "Error sending message", e)
            }
    }

    private fun loadMessages(roomId: String, userId: String) {
        val messagesRef = chatRoomsRef.child(roomId).child("messages")  // Reference to the messages node in the specific chat room
        messagesRef.orderByChild("timestamp").addChildEventListener(object : ChildEventListener {
            // This listener listens for changes (new messages) in the 'messages' node of the specified roomId.

            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val chatMessage = snapshot.getValue(ChatData::class.java)  // Convert the snapshot data to a ChatMessage object
                if (chatMessage != null) {
                    chatAdapter.addMessage(chatMessage)  // Add the message to the adapter (to update RecyclerView)
                    messagesRecyclerView.scrollToPosition(chatAdapter.itemCount - 1)
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onChildRemoved(snapshot: DataSnapshot) {}
            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}

            // This method is triggered if there is an issue with the Firebase database listener.
            override fun onCancelled(error: DatabaseError) {
                Log.e("ChatFragment", "Error loading messages: ${error.message}")
            }
        })

        chatAdapter = ChatAdapter(mutableListOf(), userId)
        messagesRecyclerView.adapter = chatAdapter
    }



}
