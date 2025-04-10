package com.example.mobile6.ui.medicine.medication_warning

import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.mobile6.databinding.FragmentMedicationWarningDialogBinding
import com.example.mobile6.ui.base.BaseDialog

class MedicationWarningDialogFragment : BaseDialog<FragmentMedicationWarningDialogBinding>() {
    override val bindingInflater: (LayoutInflater, ViewGroup?) -> FragmentMedicationWarningDialogBinding
        get() = { inflater, container ->
            FragmentMedicationWarningDialogBinding.inflate(inflater, container, false)
        }

    override fun initViews() {
    }

    override fun initObservers() {
    }

}