package com.example.mobile6.presentation.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.mobile6.databinding.ItemMessageBinding
import com.example.mobile6.domain.model.Message

class ChatMessageAdapter :
    ListAdapter<Message, ChatMessageAdapter.MessageViewHolder>(MessageDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val binding = ItemMessageBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MessageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class MessageViewHolder(
        private val binding: ItemMessageBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(message: Message) {
            if (message.isFromCurrentUser) {
                binding.sentMessageLayout.visibility = View.VISIBLE
                binding.receivedMessageLayout.visibility = View.GONE
                binding.sentMessageText.text = message.content

//                if (message.imageUrl != null) {
//                    binding.sentImage.visibility = View.VISIBLE
//                    // TODO: Load image using Glide or Coil
//                } else {
//                    binding.sentImage.visibility = View.GONE
//                }
            } else {
                binding.receivedMessageLayout.visibility = View.VISIBLE
                binding.sentMessageLayout.visibility = View.GONE
                binding.receivedMessageText.text = message.content

//                if (message.imageUrl != null) {
//                    binding.receivedImage.visibility = View.VISIBLE
//                    // TODO: Load image using Glide or Coil
//                } else {
//                    binding.receivedImage.visibility = View.GONE
//                }
            }
        }
    }
}

private class MessageDiffCallback : DiffUtil.ItemCallback<Message>() {
    override fun areItemsTheSame(oldItem: Message, newItem: Message): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Message, newItem: Message): Boolean {
        return oldItem == newItem
    }
} 