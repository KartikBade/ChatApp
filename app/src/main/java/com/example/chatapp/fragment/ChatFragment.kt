package com.example.chatapp.fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.chatapp.adapter.ChatAdapter
import com.example.chatapp.databinding.FragmentChatBinding
import com.example.chatapp.model.Message
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class ChatFragment : Fragment() {

    private lateinit var binding: FragmentChatBinding

    private lateinit var messageList: ArrayList<Message>

    private lateinit var mDatabaseRef: DatabaseReference

    private lateinit var mAuth: FirebaseAuth

    private lateinit var receiverId: String
    private lateinit var receiverName: String

    private lateinit var senderId: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChatBinding.inflate(inflater)
        messageList = ArrayList()
        mDatabaseRef = FirebaseDatabase.getInstance().reference
        mAuth = FirebaseAuth.getInstance()
        senderId = mAuth.currentUser?.uid!!

        val args: ChatFragmentArgs by navArgs()
        receiverId = args.receiverId
        receiverName = args.receiverName

        (activity as AppCompatActivity).supportActionBar?.title = receiverName

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val senderRoom = receiverId + senderId
        val receiverRoom = senderId + receiverId

        val adapter = ChatAdapter()
        binding.chatRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.chatRecyclerView.adapter = adapter

        mDatabaseRef.child("chats").child(senderRoom).child("messages").addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                messageList.clear()
                for (postSnapshot in snapshot.children) {
                    val currentMessage = postSnapshot.getValue(Message::class.java)
                    messageList.add(currentMessage!!)
                }
                adapter.submitList(messageList)
            }

            override fun onCancelled(error: DatabaseError) {}
        })

        binding.btnSend.setOnClickListener {
            val messageBoxText = binding.tvMessageBox.text.toString().trim()
            val message = Message(messageBoxText, senderId)

            mDatabaseRef.child("chats")
                .child(senderRoom)
                .child("messages")
                .push()
                .setValue(message)
                .addOnSuccessListener {
                    mDatabaseRef.child("chats")
                        .child(receiverRoom)
                        .child("messages")
                        .push()
                        .setValue(message)
                }
            binding.tvMessageBox.text.clear()
            closeKeyboard()
        }
    }

    private fun closeKeyboard() {
        activity?.currentFocus?.let {
            val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            imm?.hideSoftInputFromWindow(view?.windowToken, 0)
        }
    }
}