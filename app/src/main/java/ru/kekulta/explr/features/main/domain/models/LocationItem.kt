package ru.kekulta.explr.features.main.domain.models

import android.os.Parcelable
import androidx.recyclerview.widget.DiffUtil
import kotlinx.parcelize.Parcelize

@Parcelize
data class LocationItem(val text: String, val id: Int) : Parcelable {
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