package com.example.mobile6.ui.chat

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.mobile6.R
import com.example.mobile6.databinding.FragmentChatListDoctorBinding
import com.example.mobile6.databinding.FragmentDoctorDetailBinding
import com.example.mobile6.ui.base.BaseFragment


class ChatListDoctorFragment : BaseFragment<FragmentChatListDoctorBinding>() {
    override val bindingInflater: (LayoutInflater, ViewGroup?) -> FragmentChatListDoctorBinding
        get() = { inflater, container ->
            FragmentChatListDoctorBinding.inflate(inflater, container, false)
        }

    override fun initViews() {
    }

    override fun initObservers() {
    }
}