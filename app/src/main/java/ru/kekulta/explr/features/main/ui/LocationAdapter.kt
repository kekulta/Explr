package ru.kekulta.explr.features.main.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.kekulta.explr.databinding.FileItemBinding
import ru.kekulta.explr.databinding.LocationItemBinding
import ru.kekulta.explr.features.list.domain.models.events.FileClickEvent
import ru.kekulta.explr.features.list.ui.FilesAdapter
import ru.kekulta.explr.features.main.domain.models.LocationItem
import ru.kekulta.explr.shared.utils.gone
import ru.kekulta.explr.shared.utils.visible


class LocationAdapter :
    ListAdapter<LocationItem, LocationAdapter.Holder>(LocationItem.DIFF_CALLBACK) {
    var onClickListener: ((LocationItem) -> Unit)? = null

    inner class Holder(private val binding: LocationItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun onBind(location: LocationItem) {

            binding.root.setOnClickListener {
                onClickListener?.invoke(location)
            }

            binding.locationItem.text = location.text
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder = Holder(
        LocationItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
    )

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.onBind(getItem(position))
    }

    companion object {
        const val LOG_TAG = "FilesAdapter"
    }
}