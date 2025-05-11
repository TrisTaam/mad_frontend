package com.example.mobile6.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.mobile6.databinding.ItemMessageReceivedBinding
import com.example.mobile6.databinding.ItemMessageSentBinding
import com.example.mobile6.domain.model.ChatAIMessage
import java.text.SimpleDateFormat
import java.util.*

class ChatAIAdapter : ListAdapter<ChatAIMessage, RecyclerView.ViewHolder>(ChatAIDiffCallback()) {

    companion object {
        private const val VIEW_TYPE_USER = 1
        private const val VIEW_TYPE_AI = 2
        private val dateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_USER -> {
                val binding = ItemMessageSentBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                UserMessageViewHolder(binding)
            }
            VIEW_TYPE_AI -> {
                val binding = ItemMessageReceivedBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                AIMessageViewHolder(binding)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = getItem(position)
        when (holder) {
            is UserMessageViewHolder -> holder.bind(message)
            is AIMessageViewHolder -> holder.bind(message)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (getItem(position).isUser) VIEW_TYPE_USER else VIEW_TYPE_AI
    }

    private class UserMessageViewHolder(
        private val binding: ItemMessageSentBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(message: ChatAIMessage) {
            binding.messageText.text = message.content
            binding.timeText.text = Companion.dateFormat.format(Date(message.timestamp))
        }
    }

    private class AIMessageViewHolder(
        private val binding: ItemMessageReceivedBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(message: ChatAIMessage) {
            binding.messageText.text = message.content
            binding.timeText.text = Companion.dateFormat.format(Date(message.timestamp))
        }
    }

    private class ChatAIDiffCallback : DiffUtil.ItemCallback<ChatAIMessage>() {
        override fun areItemsTheSame(oldItem: ChatAIMessage, newItem: ChatAIMessage): Boolean {
            return oldItem.timestamp == newItem.timestamp
        }

        override fun areContentsTheSame(oldItem: ChatAIMessage, newItem: ChatAIMessage): Boolean {
            return oldItem == newItem
        }
    }
}
