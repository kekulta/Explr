package ru.kekulta.explr.features.main.domain.models

import androidx.recyclerview.widget.DiffUtil

data class LocationItem(val text: String) {
    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<LocationItem>() {
            override fun areItemsTheSame(
                oldItem: LocationItem,
                newItem: LocationItem
            ): Boolean =
                oldItem == newItem


            override fun areContentsTheSame(
                oldItem: LocationItem,
                newItem: LocationItem
            ): Boolean =
                oldItem == newItem
        }
    }
}