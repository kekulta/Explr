package ru.kekulta.explr.features.list.domain.models

import androidx.recyclerview.widget.DiffUtil
import ru.kekulta.explr.features.list.data.dto.FileDto
import java.io.File

data class FileRepresentation(
    var path: String,
    var name: String,
    var isDirectory: Boolean,
    var hashcode: Int,
    var parent: String,
    var level: Int,
) {
    constructor(file: FileDto) : this(
        file.path,
        file.name,
        file.isDirectory,
        file.hashcode,
        file.parent ?: ROOT,
        file.level,
    )

    constructor(file: File, level: Int) : this(
        file.path,
        file.name,
        file.isDirectory,
        file.hashCode(),
        file.parent ?: ROOT,
        level,
    )


    fun toDto(): FileDto =
        FileDto(path, name, isDirectory, hashcode, if (parent == ROOT) null else parent, level)

    companion object {
        const val ROOT = "root"

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