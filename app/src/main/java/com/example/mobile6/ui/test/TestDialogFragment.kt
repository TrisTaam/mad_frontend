package com.example.mobile6.ui.test

import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.mobile6.databinding.FragmentTestDialogBinding
import com.example.mobile6.ui.base.BaseDialog

class TestDialogFragment : BaseDialog<FragmentTestDialogBinding>() {
    override val bindingInflater: (LayoutInflater, ViewGroup?) -> FragmentTestDialogBinding
        get() = { inflater, container ->
            FragmentTestDialogBinding.inflate(inflater, container, false)
        }

    override fun initViews() {
        binding.btnOk.setOnClickListener {
            binding.edtTest.text.toString().takeIf { it.isNotBlank() }.let { message ->
                dismiss("test", message)
            }
        }
    }

    override fun initObservers() {

    }
}