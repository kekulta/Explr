package ru.kekulta.explr.features.difflist.domain.models

import androidx.recyclerview.widget.DiffUtil
import ru.kekulta.explr.features.difflist.data.database.dto.HashedDto
import ru.kekulta.explr.features.difflist.domain.models.enums.ChangeType
import ru.kekulta.explr.features.list.domain.models.FileRepresentation
import ru.kekulta.explr.features.list.domain.models.enums.FileType

data class HashedFile(
    val path: String,
    val name: String,
    val type: FileType,
    val hash: String?,
    val changeType: ChangeType,
    val hidden: Boolean,
    val nomedia: Boolean,
) {
    constructor(file: HashedDto) : this(
        path = file.path,
        name = file.name,
        type = file.type,
        hash = file.hash,
        changeType = file.changeType,
        hidden = file.hidden,
        nomedia = file.nomedia,
    )

    fun toDto(): HashedDto = HashedDto(
        path = path,
        name = name,
        type = type,
        hash = hash,
        changeType = changeType,
        hidden = hidden,
        nomedia = nomedia,
    )

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<HashedFile>() {
            override fun areItemsTheSame(
                oldItem: HashedFile,
                newItem: HashedFile
            ): Boolean =
                oldItem.path == newItem.path


            override fun areContentsTheSame(
                oldItem: HashedFile,
                newItem: HashedFile
            ): Boolean =
                oldItem == newItem
        }
    }
}
