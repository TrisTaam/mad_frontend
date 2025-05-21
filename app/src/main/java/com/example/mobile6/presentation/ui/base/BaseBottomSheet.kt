package com.example.mobile6.presentation.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

abstract class BaseBottomSheet<VB : ViewBinding> : BottomSheetDialogFragment() {
    private var _binding: VB? = null
    protected val binding get() = _binding!!

    protected abstract val bindingInflater: (layoutInflater: LayoutInflater, container: ViewGroup?) -> VB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = bindingInflater.invoke(inflater, container)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let { processArguments(it) }
        initViews()
        initObservers()
    }

    abstract fun initViews()

    abstract fun initObservers()

    protected open fun processArguments(args: Bundle) {

    }

    protected fun back() {
        findNavController().navigateUp()
    }

    protected fun navigateTo(
        actionId: Int,
        args: Bundle? = null,
        navOptions: NavOptions? = null
    ) {
        findNavController().navigate(actionId, args, navOptions)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}