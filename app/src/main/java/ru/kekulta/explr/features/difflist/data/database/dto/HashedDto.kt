package ru.kekulta.explr.features.difflist.data.database.dto

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

import ru.kekulta.explr.features.difflist.domain.models.enums.ChangeType
import ru.kekulta.explr.features.list.domain.models.enums.FileType

@Entity(tableName = HashedDto.TABLE)
class HashedDto(
    @PrimaryKey
    @ColumnInfo(name = PATH)
    val path: String,
    @ColumnInfo(name = NAME)
    val name: String,
    @ColumnInfo(name = TYPE)
    val type: FileType,
    @ColumnInfo(name = HASH)
    val hash: String?,
    @ColumnInfo(name = CHANGE_TYPE)
    val changeType: ChangeType,
) {
    companion object {
        const val TABLE = "hashed_table"
        const val PATH = "hashed_path"
        const val NAME = "hashed_name"
        const val TYPE = "hashed_type"
        const val HASH = "hashed_hash"
        const val CHANGE_TYPE = "hashed_change_type"
    }
}