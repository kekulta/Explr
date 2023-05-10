package ru.kekulta.explr.features.list.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import ru.kekulta.explr.features.list.data.database.dto.FileDto
import ru.kekulta.explr.features.list.domain.models.enums.FileType

@Dao
interface FileDao {

    @Query(
        """
        SELECT EXISTS (SELECT * FROM ${FileDto.TABLE} 
        WHERE ${FileDto.PATH} = :path)
        """
    )
    suspend fun isExist(path: String): Boolean


    @Query(
        """
        DELETE 
        FROM ${FileDto.TABLE}
        WHERE ${FileDto.PATH} = :path
        """
    )
    suspend fun delete(path: String)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(dto: FileDto)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(dto: FileDto)

    @Query(
        """
        SELECT * 
        FROM ${FileDto.TABLE}
        WHERE ${FileDto.PARENT} = :path
        """
    )
    suspend fun getContent(path: String): List<FileDto>

    @Query(
        """
        SELECT * 
        FROM ${FileDto.TABLE}
        WHERE ${FileDto.PATH} = :path
        """
    )
    suspend fun get(path: String): FileDto?

    @Query(
        """
        SELECT * 
        FROM ${FileDto.TABLE}
        WHERE ${FileDto.PARENT} = :path AND ((NOT ${FileDto.IS_HIDDEN}) OR :hidden) AND ((NOT ${FileDto.IS_NOMEDIA}) OR :nomedia)
        """
    )
    fun observeContent(path: String, hidden: Boolean, nomedia: Boolean): Flow<List<FileDto>>

    @Query(
        """
        SELECT * 
        FROM ${FileDto.TABLE}
        WHERE ${FileDto.TYPE} = :type AND ((NOT ${FileDto.IS_HIDDEN}) OR :hidden) AND ((NOT ${FileDto.IS_NOMEDIA}) OR :nomedia)
        """
    )
    fun observeType(type: FileType, hidden: Boolean, nomedia: Boolean): Flow<List<FileDto>>

    @Query(
        """
        SELECT * 
        FROM ${FileDto.TABLE}
        WHERE ${FileDto.LAST_MODIFIED} > :from AND ((NOT ${FileDto.IS_HIDDEN}) OR :hidden) AND ((NOT ${FileDto.IS_NOMEDIA}) OR :nomedia)
        """
    )
    fun observeRecent(from: Long, hidden: Boolean, nomedia: Boolean): Flow<List<FileDto>>

    @Query("""
        UPDATE ${FileDto.TABLE}
        SET ${FileDto.SIZE} = :size 
        WHERE ${FileDto.PATH}=:path
        """)
    fun updateSize(path: String, size: Double)
}