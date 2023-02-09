package com.example.chatapp.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.chatapp.R
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.chatapp.adapter.UserListAdapter
import com.example.chatapp.databinding.FragmentListBinding
import com.example.chatapp.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class ListFragment : Fragment() {

    private lateinit var binding: FragmentListBinding

    private lateinit var mDatabaseRef: DatabaseReference

    private lateinit var mAuth: FirebaseAuth

    private lateinit var userList: ArrayList<User>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentListBinding.inflate(inflater)
        mDatabaseRef = FirebaseDatabase.getInstance().reference
        mAuth = FirebaseAuth.getInstance()
        userList = ArrayList()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val userListAdapter = UserListAdapter {
            val action = ListFragmentDirections.actionListFragmentToChatFragment(it.uid.toString())
            findNavController().navigate(action)
        }

        binding.listRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.listRecyclerView.adapter = userListAdapter

        mDatabaseRef.child("user").addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                userList.clear()
                for (postSnapshot in snapshot.children) {
                    val currentUser = postSnapshot.getValue(User::class.java)
                    if (currentUser?.uid != mAuth.currentUser?.uid) {
                        userList.add(currentUser!!)
                    }
                }
                userListAdapter.submitList(userList)
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }
}