package ru.kekulta.explr.features.list.data.dto

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
import androidx.room.PrimaryKey
import ru.kekulta.explr.features.list.data.dto.FileDto.Companion.PARENT
import ru.kekulta.explr.features.list.data.dto.FileDto.Companion.PATH
import ru.kekulta.explr.features.list.data.dto.FileDto.Companion.TABLE

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
) {


    companion object {
        const val TABLE = "file_table"
        const val PATH = "file_path"
        const val NAME = "file_name"
        const val IS_DIRECTORY = "file_is_directory"
        const val HASHCODE = "file_hashcode"
        const val PARENT = "file_parent"
        const val LEVEL = "file_level"
    }
}