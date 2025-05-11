package com.example.mobile6.ui.chat.doctor

import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.mobile6.databinding.FragmentChatItemDoctorInDoctorListBinding
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