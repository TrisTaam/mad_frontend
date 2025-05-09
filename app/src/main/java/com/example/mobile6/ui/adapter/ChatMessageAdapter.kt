package com.example.mobile6.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mobile6.databinding.ItemChatMessageBinding
import com.example.mobile6.domain.model.Message
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class ChatMessageAdapter : RecyclerView.Adapter<ChatMessageAdapter.MessageViewHolder>() {
    
    private val messages = mutableListOf<Message>()
    private var currentUserId: Long = 0

    fun submitList(newList: List<Message>, userId: Long) {
        messages.clear()
        messages.addAll(newList)
        currentUserId = userId
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val binding = ItemChatMessageBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return MessageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        holder.bind(messages[position])
    }

    override fun getItemCount() = messages.size

    inner class MessageViewHolder(private val binding: ItemChatMessageBinding) :
        RecyclerView.ViewHolder(binding.root) {
        
        fun bind(message: Message) {
            val isSentByCurrentUser = message.senderId == currentUserId
            
            if (isSentByCurrentUser) {
                binding.apply {
                    tvMessageSent.text = message.content
                    tvTimeSent.text = formatDateTime(message.sentAt)
                    // Show sent message layout
                    layoutSentMessage.visibility = View.VISIBLE
                    layoutReceivedMessage.visibility = View.GONE
                }
            } else {
                binding.apply {
                    tvMessageReceived.text = message.content
                    tvTimeReceived.text = formatDateTime(message.sentAt)
                    // Show received message layout
                    layoutSentMessage.visibility = View.GONE
                    layoutReceivedMessage.visibility = View.VISIBLE
                }
            }
        }

        private fun formatDateTime(dateTime: LocalDateTime): String {
            return dateTime.format(DateTimeFormatter.ofPattern("HH:mm"))
        }
    }
}
