package ru.kekulta.explr.features.list.data.dto

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
import androidx.room.PrimaryKey
import ru.kekulta.explr.features.list.data.dto.FileDto.Companion.PARENT
import ru.kekulta.explr.features.list.data.dto.FileDto.Companion.PATH
import ru.kekulta.explr.features.list.data.dto.FileDto.Companion.TABLE
import ru.kekulta.explr.features.list.domain.models.enums.FileType

@Entity(
    tableName = TABLE,
    foreignKeys = [ForeignKey(
        entity = FileDto::class,
        parentColumns = [PATH],
        childColumns = [PARENT],
        onDelete = CASCADE,
        onUpdate = CASCADE
    )]
)
class FileDto(
    @PrimaryKey
    @ColumnInfo(name = PATH)
    var path: String,
    @ColumnInfo(name = NAME)
    val name: String,
    @ColumnInfo(name = IS_DIRECTORY)
    var isDirectory: Boolean,
    @ColumnInfo(name = HASHCODE)
    var hashcode: Int,
    @ColumnInfo(name = PARENT)
    var parent: String?,
    @ColumnInfo(name = LEVEL)
    var level: Int,
    @ColumnInfo(name = TYPE)
    var type: FileType,
    @ColumnInfo(name = LAST_MODIFIED)
    var lastModified: Long,
    @ColumnInfo(name = IS_HIDDEN)
    var isHidden: Boolean,
    @ColumnInfo(name = IS_NOMEDIA)
    var isNoMedia: Boolean,
    @ColumnInfo(name = SIZE)
    var size: Double,
) {


    companion object {
        const val TABLE = "file_table"
        const val PATH = "file_path"
        const val NAME = "file_name"
        const val IS_DIRECTORY = "file_is_directory"
        const val HASHCODE = "file_hashcode"
        const val PARENT = "file_parent"
        const val LEVEL = "file_level"
        const val TYPE = "file_type"
        const val LAST_MODIFIED = "file_last_modified"
        const val IS_HIDDEN = "file_is_hidden"
        const val IS_NOMEDIA = "file_nomedia"
        const val SIZE = "file_size"
    }
}