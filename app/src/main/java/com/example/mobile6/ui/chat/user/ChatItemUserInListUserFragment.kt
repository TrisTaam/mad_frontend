package com.example.mobile6.ui.chat.user

import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.mobile6.databinding.FragmentChatItemUserInListUserBinding
import com.example.mobile6.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChatItemUserInListUserFragment : BaseFragment<FragmentChatItemUserInListUserBinding>() {
    override val bindingInflater: (LayoutInflater, ViewGroup?) -> FragmentChatItemUserInListUserBinding
        get() = { inflater, container ->
            FragmentChatItemUserInListUserBinding.inflate(inflater, container, false)
        }

    override fun initViews() {

    }

    override fun initObservers() {
    }
}