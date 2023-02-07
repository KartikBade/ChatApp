package com.example.chatapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.chatapp.databinding.ReceivedMessageBinding
import com.example.chatapp.databinding.SentMessageBinding
import com.example.chatapp.model.Message
import com.google.firebase.auth.FirebaseAuth

class ChatAdapter: ListAdapter<Message, ViewHolder>(DiffCallback) {

    private val ITEM_SENT = 1
    private val ITEM_RECEIVED = 2

    class SentViewHolder(private val binding: SentMessageBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(currentMessage: Message) {
            binding.sentMessage.text = currentMessage.message
        }
    }

    class ReceivedViewHolder(private val binding: ReceivedMessageBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(currentMessage: Message) {
            binding.receivedMessage.text = currentMessage.message
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        if (viewType == ITEM_SENT) {
            return SentViewHolder(SentMessageBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        } else {
            return ReceivedViewHolder(ReceivedMessageBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentMessage = getItem(position)

        if (holder.javaClass == SentViewHolder::class.java) {
            val currentHolder = holder as SentViewHolder
            currentHolder.bind(currentMessage)
        } else {
            val currentHolder = holder as ReceivedViewHolder
            currentHolder.bind(currentMessage)
        }
    }

    companion object DiffCallback: DiffUtil.ItemCallback<Message>() {
        override fun areItemsTheSame(oldItem: Message, newItem: Message): Boolean {
            return oldItem.message == newItem.message
        }

        override fun areContentsTheSame(oldItem: Message, newItem: Message): Boolean {
            return oldItem.sender == newItem.sender
        }
    }

    override fun getItemViewType(position: Int): Int {
        val currentMessage = getItem(position)

        if (currentMessage.sender == FirebaseAuth.getInstance().currentUser?.uid) {
            return ITEM_SENT
        }
        return ITEM_RECEIVED
    }
}