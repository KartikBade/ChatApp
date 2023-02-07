package com.example.chatapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.chatapp.databinding.UserListItemBinding
import com.example.chatapp.model.User

class UserListAdapter(private val listener: (User) -> Unit): ListAdapter<User, UserListAdapter.UserListViewHolder>(DiffCallback) {

    class UserListViewHolder(val binding: UserListItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(currentUser: User) {
            binding.nameTv.text = currentUser.name.toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserListViewHolder {
        return UserListViewHolder(UserListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: UserListViewHolder, position: Int) {
        val currentUser = getItem(position)
        holder.bind(currentUser)
        holder.binding.nameTv.setOnClickListener {
            listener(currentUser)
        }
    }

    companion object DiffCallback: DiffUtil.ItemCallback<User>() {
        override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem.uid == newItem.uid
        }

        override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem.name == newItem.name && oldItem.email == newItem.email
        }
    }
}