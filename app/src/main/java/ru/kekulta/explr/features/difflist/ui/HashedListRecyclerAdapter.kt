package ru.kekulta.explr.features.difflist.ui

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.kekulta.explr.R
import ru.kekulta.explr.databinding.ChangedFileItemBinding
import ru.kekulta.explr.databinding.FileItemBinding
import ru.kekulta.explr.features.difflist.domain.models.HashedFile
import ru.kekulta.explr.features.list.domain.models.FileRepresentation
import ru.kekulta.explr.features.list.domain.models.enums.FileType
import ru.kekulta.explr.features.list.domain.models.events.FileClickEvent
import ru.kekulta.explr.shared.utils.dayOfMonth
import ru.kekulta.explr.shared.utils.getMonth
import ru.kekulta.explr.shared.utils.gone
import ru.kekulta.explr.shared.utils.month
import ru.kekulta.explr.shared.utils.sizeInKb
import ru.kekulta.explr.shared.utils.visible


class HashedListRecyclerAdapter :
    ListAdapter<HashedFile, HashedListRecyclerAdapter.Holder>(HashedFile.DIFF_CALLBACK) {
    var onClickListener: ((HashedFile) -> Unit)? = null
    inner class Holder(private val binding: ChangedFileItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun onBind(file: HashedFile) {

            binding.root.setOnClickListener { onClickListener?.invoke(file) }

            binding.changedFileName.text = file.name

            binding.changedFileIcon.setImageResource(
                when (file.type) {
                    FileType.DIRECTORY -> R.drawable.directory_icon
                    FileType.FILE -> R.drawable.file_icon
                    FileType.IMAGE -> R.drawable.picture_icon
                    FileType.DOCUMENT -> R.drawable.document_icon
                    FileType.VIDEO -> R.drawable.video_icon
                    FileType.AUDIO -> R.drawable.audio_icon
                }
            )
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder = Holder(
        ChangedFileItemBinding.inflate(
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