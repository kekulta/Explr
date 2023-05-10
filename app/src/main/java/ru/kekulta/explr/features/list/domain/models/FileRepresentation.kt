package ru.kekulta.explr.features.list.domain.models

import androidx.recyclerview.widget.DiffUtil
import ru.kekulta.explr.features.list.data.database.dto.FileDto
import ru.kekulta.explr.features.list.domain.models.enums.FileType
import ru.kekulta.explr.shared.utils.size

import java.io.File

data class FileRepresentation(
    val path: String,
    val name: String,
    val isDirectory: Boolean,
    val hashcode: Int,
    val parent: String?,
    val level: Int,
    val type: FileType,
    val lastModified: Long,
    val isHidden: Boolean,
    val isNoMedia: Boolean,
    val size: Double,
) {
    constructor(file: FileDto) : this(
        path = file.path,
        name = file.name,
        isDirectory = file.isDirectory,
        hashcode = file.hashcode,
        parent = file.parent,
        level = file.level,
        type = file.type,
        lastModified = file.lastModified,
        isHidden = file.isHidden,
        isNoMedia = file.isNoMedia,
        size = file.size
    )

    constructor(file: File, level: Int, isHidden: Boolean, isNoMedia: Boolean, type: FileType) : this(
        path = file.path,
        name = file.name,
        isDirectory = file.isDirectory,
        hashcode = file.hashCode(),
        parent = file.parent,
        level = level,
        type = type,
        lastModified = file.lastModified(),
        isHidden = isHidden,
        isNoMedia = isNoMedia,
        size = file.size
    )


    fun toDto(): FileDto =
        FileDto(
            path = path,
            name = name,
            isDirectory = isDirectory,
            hashcode = hashcode,
            parent = parent,
            level = level,
            type = type,
            lastModified = lastModified,
            isHidden = isHidden,
            isNoMedia = isNoMedia,
            size = size
        )

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<FileRepresentation>() {
            override fun areItemsTheSame(
                oldItem: FileRepresentation,
                newItem: FileRepresentation
            ): Boolean =
                oldItem.path == newItem.path


            override fun areContentsTheSame(
                oldItem: FileRepresentation,
                newItem: FileRepresentation
            ): Boolean =
                oldItem == newItem
        }
    }
}