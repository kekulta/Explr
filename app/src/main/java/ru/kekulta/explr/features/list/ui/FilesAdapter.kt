package ru.kekulta.explr.features.list.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.kekulta.explr.R
import ru.kekulta.explr.databinding.FileItemBinding
import ru.kekulta.explr.features.list.domain.models.FileRepresentation
import ru.kekulta.explr.shared.utils.FileType

class FilesAdapter :
    ListAdapter<FileRepresentation, FilesAdapter.Holder>(FileRepresentation.DIFF_CALLBACK) {
    //TODO fix listener
    var onClickListener: ((String) -> Unit)? = null


    inner class Holder(private val binding: FileItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun onBind(file: FileRepresentation) {
            binding.root.setOnClickListener {
                onClickListener?.invoke(file.path)
            }
            binding.fileName.text =
                "${file.name} isHidden:${file.isHidden} isNomedia: ${file.isNoMedia}"
            binding.fileIcon.setImageResource(
                when (file.type) {
                    FileType.DIRECTORY -> R.drawable.directory_icon
                    FileType.FILE -> R.drawable.file_icon
                    FileType.IMAGE -> R.drawable.picture_icon
                    else -> R.drawable.file_icon
                }
            )
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder = Holder(
        FileItemBinding.inflate(
            LayoutInflater.from(parent.context)
        )
    )

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.onBind(getItem(position))
    }
}