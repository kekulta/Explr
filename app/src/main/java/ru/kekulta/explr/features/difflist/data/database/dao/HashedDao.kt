package ru.kekulta.explr.features.difflist.data.database.dao


import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import ru.kekulta.explr.features.difflist.data.database.dto.HashedDto
import ru.kekulta.explr.features.difflist.domain.models.enums.ChangeType
import ru.kekulta.explr.features.list.data.database.dto.FileDto
import ru.kekulta.explr.features.list.domain.models.enums.FileType

@Dao
interface HashedDao {

    @Query(
        """
        SELECT EXISTS (SELECT * FROM ${HashedDto.TABLE} 
        WHERE ${HashedDto.PATH} = :path)
        """
    )
    suspend fun isExist(path: String): Boolean


    @Query(
        """
        DELETE 
        FROM ${HashedDto.TABLE}
        WHERE ${HashedDto.PATH} = :path
        """
    )
    suspend fun delete(path: String)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(dto: HashedDto)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(dto: HashedDto)

    @Query(
        """
        SELECT * 
        FROM ${HashedDto.TABLE}
        WHERE ${HashedDto.PATH} = :path
        """
    )
    suspend fun get(path: String): HashedDto?

    @Query(
        """
        DELETE 
        FROM ${HashedDto.TABLE}
        WHERE ${HashedDto.CHANGE_TYPE} = :type
        """
    )
    suspend fun deleteAllType(type: ChangeType)

    @Query(
        """
        UPDATE ${HashedDto.TABLE}
        SET ${HashedDto.CHANGE_TYPE} = :type
        """
    )
    suspend fun setAllType(type: ChangeType)

    @Query(
        """
        UPDATE ${HashedDto.TABLE}
        SET ${HashedDto.CHANGE_TYPE} = :to 
        WHERE ${HashedDto.CHANGE_TYPE}=:from
        """
    )
    suspend fun changeAllType(from: ChangeType, to: ChangeType)

    @Query(
        """
        SELECT * 
        FROM ${HashedDto.TABLE}
        WHERE ${HashedDto.CHANGE_TYPE} = :type
        """
    )
    fun observeType(type: ChangeType): Flow<List<HashedDto>>
}