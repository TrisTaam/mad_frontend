package com.example.mobile6.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.example.mobile6.R
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

abstract class BaseDialog<VB : ViewBinding> : DialogFragment() {
    private var _binding: VB? = null
    protected val binding get() = _binding!!

    protected abstract val bindingInflater: (layoutInflater: LayoutInflater, container: ViewGroup?) -> VB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, R.style.rounded_dialog_style);
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

    protected fun <T> dismiss(requestKey: String, value: T) {
        findNavController().previousBackStackEntry?.savedStateHandle?.set(requestKey, value)
        dismiss()
    }

    protected fun <T> observeBackResult(requestKey: String, onResult: (T) -> Unit) {
        findNavController().currentBackStackEntry?.savedStateHandle?.getStateFlow<T?>(
            requestKey,
            null
        )
            ?.onEach {
                it?.let { value ->
                    onResult(value)
                }
            }?.flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED)
            ?.launchIn(viewLifecycleOwner.lifecycleScope)
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