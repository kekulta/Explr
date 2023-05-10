package ru.kekulta.explr.features.difflist.domain.api

import kotlinx.coroutines.flow.Flow
import ru.kekulta.explr.features.difflist.domain.models.HashedFile
import ru.kekulta.explr.features.difflist.domain.models.enums.ChangeType

interface HashedRepository {
    fun observeNew(): Flow<List<HashedFile>>
    fun observeOld(): Flow<List<HashedFile>>
    fun observeChanged(): Flow<List<HashedFile>>
    fun observeDeleted(): Flow<List<HashedFile>>
    suspend fun get(path: String): HashedFile?
    suspend fun isExist(path: String): Boolean
    suspend fun delete(file: HashedFile)
    suspend fun delete(path: String)
    suspend fun insert(file: HashedFile)
    suspend fun update(file: HashedFile)
    suspend fun setAllType(type: ChangeType)
    suspend fun deleteAllType(type: ChangeType)
    suspend fun changeAllType(from: ChangeType, to: ChangeType)
}