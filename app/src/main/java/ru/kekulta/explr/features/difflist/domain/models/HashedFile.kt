package ru.kekulta.explr.features.difflist.domain.models

import ru.kekulta.explr.features.difflist.data.database.dto.HashedDto
import ru.kekulta.explr.features.difflist.domain.models.enums.ChangeType
import ru.kekulta.explr.features.list.domain.models.enums.FileType

data class HashedFile(
    val path: String,
    val name: String,
    val type: FileType,
    val hash: String?,
    val changeType: ChangeType,
) {
    constructor(file: HashedDto) : this(
        path = file.path,
        name = file.name,
        type = file.type,
        hash = file.hash,
        changeType = file.changeType
    )

    fun toDto(): HashedDto = HashedDto(
        path = path,
        name = name,
        type = type,
        hash = hash,
        changeType = changeType
    )
}