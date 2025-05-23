package com.example.mobile6.presentation.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.fragment.app.viewModels
import com.example.mobile6.databinding.FragmentGenderSelectDialogBinding
import com.example.mobile6.presentation.ui.base.BaseDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GenderSelectDialogFragment : BaseDialog<FragmentGenderSelectDialogBinding>() {

    private var currentGender: String = ""
    private val viewModel: ProfileCompletionViewModel by viewModels(
        ownerProducer = { requireParentFragment().childFragmentManager.fragments.first() }
    )

    override val bindingInflater: (LayoutInflater, ViewGroup?) -> FragmentGenderSelectDialogBinding
        get() = { inflater, container ->
            FragmentGenderSelectDialogBinding.inflate(inflater, container, false)
        }

    override fun processArguments(args: Bundle) {
        when (currentGender) {
            "Nam" -> binding.rbMale.isChecked = true
            "Nữ" -> binding.rbFemale.isChecked = true
            "Khác" -> binding.rbOther.isChecked = true
        }
    }

    override fun initViews() {
        binding.btnCancel.setOnClickListener {
            this.dismiss()
        }
        binding.btnConfirm.setOnClickListener {
            val selectedId = binding.rgGender.checkedRadioButtonId
            if (selectedId != -1) {
                val radioButton = binding.root.findViewById<RadioButton>(selectedId)
                viewModel.setGender(radioButton.text.toString())
            }
            this.dismiss()
        }
    }

    override fun initObservers() {
    }
}