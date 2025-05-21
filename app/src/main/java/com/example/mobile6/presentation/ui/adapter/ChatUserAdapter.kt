package com.example.mobile6.presentation.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mobile6.data.remote.dto.response.ChatUserInfoResponse
import com.example.mobile6.databinding.ItemChatUserInListUserBinding

class ChatUserAdapter(
    private val onUserClick: (ChatUserInfoResponse) -> Unit
) : RecyclerView.Adapter<ChatUserAdapter.UserViewHolder>() {

    private val users = mutableListOf<ChatUserInfoResponse>()

    fun submitList(newList: List<ChatUserInfoResponse>) {
        users.clear()
        users.addAll(newList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val binding = ItemChatUserInListUserBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return UserViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(users[position])
    }

    override fun getItemCount() = users.size

    inner class UserViewHolder(private val binding: ItemChatUserInListUserBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(user: ChatUserInfoResponse) {
            binding.apply {
                tvChatUserName.text = "${user.firstName} ${user.lastName}"
                // Set other user info as needed
                // If you have user avatar, you can load it here

                root.setOnClickListener { onUserClick(user) }
            }
        }
    }
}
