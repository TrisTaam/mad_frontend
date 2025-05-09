package com.example.mobile6.ui.chat

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.mobile6.R
import com.example.mobile6.databinding.FragmentChatItemDoctorInDoctorListBinding
import com.example.mobile6.databinding.FragmentChatListDoctorBinding
import com.example.mobile6.ui.base.BaseFragment


class ChatItemDoctorInDoctorListFragment : BaseFragment<FragmentChatItemDoctorInDoctorListBinding>() {
    override val bindingInflater: (LayoutInflater, ViewGroup?) -> FragmentChatItemDoctorInDoctorListBinding
        get() = { inflater, container ->
            FragmentChatItemDoctorInDoctorListBinding.inflate(inflater, container, false)
        }

    override fun initViews() {

    }

    override fun initObservers() {
    }
}