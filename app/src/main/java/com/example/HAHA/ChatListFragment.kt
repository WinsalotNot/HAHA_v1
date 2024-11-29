package com.example.HAHA

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.HAHA.Adapter.ChatListAdapter
import com.example.HAHA.Data.ChatData
import com.example.HAHA.Data.ChatRoomData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ChatListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ChatListFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var chatRoomRecyclerView: RecyclerView
    private lateinit var chatRoomAdapter: ChatListAdapter
    private val chatRoomsRef = FirebaseDatabase.getInstance("https://testing-6341f-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("chatRooms")

    private val userId = "BlackMamba1" // TODO: Replace with the logged-in user's ID

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_chat_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        chatRoomRecyclerView = view.findViewById(R.id.chatRoomRecyclerView)
        chatRoomRecyclerView.layoutManager = LinearLayoutManager(context)
        chatRoomAdapter = ChatListAdapter(mutableListOf()) { chatRoom ->
            // Handle chat room click
            openChatRoom(chatRoom)
        }
        chatRoomRecyclerView.adapter = chatRoomAdapter

        loadChatRooms()
    }

    private fun loadChatRooms() {
        chatRoomsRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (child in snapshot.children) {
                    val chatRoom = child.getValue(ChatRoomData::class.java)
                    if (chatRoom != null && chatRoom.members.contains(userId)) {
                        val roomId = chatRoom.roomId
                        val messagesRef = chatRoomsRef.child(roomId).child("messages")
                        messagesRef.limitToLast(1).addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(messageSnapshot: DataSnapshot) {
                                var lastMessageText = "No messages yet."
                                var name = "Unknown" // Default value for name

                                // Get the latest message and extract sender/receiver info
                                for (messageChild in messageSnapshot.children) {
                                    val lastMessage = messageChild.getValue(ChatData::class.java)
                                    if (lastMessage != null) {
                                        lastMessageText = lastMessage.message

                                        // Check if userId matches senderId or receiverId to set the name
                                        if (userId == lastMessage.senderId) {
                                            name = lastMessage.receiverName ?: "Receiver" // Set receiverName
                                        } else {
                                            name = lastMessage.senderName ?: "Sender" // Set senderName
                                        }
                                    }
                                }

                                // Pass chatRoom, lastMessageText, and the correct name to the adapter
                                chatRoomAdapter.addChatRoom(chatRoom, lastMessageText, name)
                            }

                            override fun onCancelled(error: DatabaseError) {
                                Log.e("ChatRoomsFragment", "Error loading last message: ${error.message}")
                            }
                        })
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("ChatRoomsFragment", "Error loading chat rooms: ${error.message}")
            }
        })
    }


    private fun openChatRoom(chatRoom: ChatRoomData) {
        val navController = findNavController() // Get the NavController
        val roomId = chatRoom.roomId

        // Use a ValueEventListener to fetch the values from Firebase
        chatRoomsRef.child(roomId).child("messages").limitToLast(1).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // Check if there are messages
                if (snapshot.exists()) {
                    // Get the latest message
                    val latestMessageSnapshot = snapshot.children.firstOrNull()

                    // If there is a message, extract sender and receiver info
                    if (latestMessageSnapshot != null) {
                        val senderId = latestMessageSnapshot.child("senderId").getValue(String::class.java)
                        val receiverId = latestMessageSnapshot.child("receiverId").getValue(String::class.java)
                        val senderName = latestMessageSnapshot.child("senderName").getValue(String::class.java)
                        val receiverName = latestMessageSnapshot.child("receiverName").getValue(String::class.java)

                        // Log the values to verify they are correct
                        Log.e("ChatRoomsFragment", "Sender ID: $senderId, Receiver ID: $receiverId")
                        Log.e("ChatRoomsFragment", "Sender Name: $senderName, Receiver Name: $receiverName")

                        val bundle = Bundle().apply {
                            if (userId == senderId) {
                                // If the user is the sender, pass the receiver's info
                                putString("creatorId", receiverId)  // Pass the receiver's ID
                                putString("name", receiverName)     // Pass the receiver's name
                                Log.e("ChatRoomsFragment", "creatorID: $receiverId") // Log receiver info
                            } else {
                                // If the user is the receiver, pass the sender's info
                                putString("creatorId", senderId)   // Pass the sender's ID
                                putString("name", senderName)       // Pass the sender's name
                                Log.e("ChatRoomsFragment", "creatorID: $senderId")  // Log sender info
                            }
                        }

                        // Navigate to the chat fragment with the data
                        navController.navigate(R.id.action_chatListFragment_to_chatFragment, bundle)
                    }
                } else {
                    Log.e("ChatRoomsFragment", "No messages found in the chat room")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle any errors that occur when fetching data
                Log.e("ChatFragment", "Error fetching data: ${error.message}")
            }
        })
    }




}