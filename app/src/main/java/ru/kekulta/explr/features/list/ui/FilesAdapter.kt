package ru.kekulta.explr.features.list.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.kekulta.explr.R
import ru.kekulta.explr.databinding.FileItemBinding
import ru.kekulta.explr.features.list.domain.models.FileRepresentation
import ru.kekulta.explr.shared.utils.FileType
import ru.kekulta.explr.shared.utils.dayOfMonth
import ru.kekulta.explr.shared.utils.getMonth
import ru.kekulta.explr.shared.utils.gone
import ru.kekulta.explr.shared.utils.month
import ru.kekulta.explr.shared.utils.sizeInKb
import ru.kekulta.explr.shared.utils.visible

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
            binding.fileName.text = file.name
            if (!file.isDirectory) {
                binding.fileDetails.visible()
                binding.root.context.resources.let { resources ->

                    binding.fileSize.text = resources.getString(R.string.file_size_placeholder)
                        .format(file.sizeInKb.toInt())

                    binding.fileLastModified.text =
                        resources.getString(R.string.month_day_placeholder).format(
                            resources.getMonth(month(file.lastModified)),
                            dayOfMonth(file.lastModified)
                        )
                }


            } else {
                binding.fileDetails.gone()
            }
            binding.fileIcon.setImageResource(
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
        FileItemBinding.inflate(
            LayoutInflater.from(parent.context)
        )
    )

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.onBind(getItem(position))
    }
}