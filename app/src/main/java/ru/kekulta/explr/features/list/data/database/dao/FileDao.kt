package ru.kekulta.explr.features.list.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import ru.kekulta.explr.features.list.data.dto.FileDto

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

    @Update
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
        WHERE ${FileDto.PARENT} = :path
        """
    )
    fun observeContent(path: String): Flow<List<FileDto>>

}