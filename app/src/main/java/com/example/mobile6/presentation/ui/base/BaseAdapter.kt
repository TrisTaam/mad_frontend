package com.example.mobile6.presentation.ui.base

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

abstract class BaseAdapter<T, VH : RecyclerView.ViewHolder>(
    diffCallback: DiffUtil.ItemCallback<T>
) : ListAdapter<T, VH>(diffCallback) {
    companion object {
        inline fun <reified T : Any> simpleDiffCallback(
            crossinline areItemsTheSame: (T, T) -> Boolean = { old, new -> old == new },
            crossinline areContentsTheSame: (T, T) -> Boolean = { old, new -> old == new }
        ): DiffUtil.ItemCallback<T> = object : DiffUtil.ItemCallback<T>() {
            override fun areItemsTheSame(oldItem: T, newItem: T): Boolean =
                areItemsTheSame(oldItem, newItem)

            override fun areContentsTheSame(oldItem: T, newItem: T): Boolean =
                areContentsTheSame(oldItem, newItem)
        }
    }
}