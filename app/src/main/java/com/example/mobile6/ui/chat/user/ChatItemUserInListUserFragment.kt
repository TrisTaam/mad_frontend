package com.example.mobile6.ui.chat.user

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.mobile6.databinding.FragmentChatItemUserInListUserBinding
import com.example.mobile6.data.remote.dto.response.ChatUserInfoResponse
import com.example.mobile6.databinding.FragmentChatItemDoctorInDoctorListBinding
import com.example.mobile6.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

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