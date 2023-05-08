package ru.kekulta.explr.features.list.domain.api

import kotlinx.coroutines.flow.Flow
import ru.kekulta.explr.features.list.domain.models.FileRepresentation
import ru.kekulta.explr.shared.utils.FileType

interface FilesRepository {
    fun observeRecent(): Flow<List<FileRepresentation>>
    fun observeContent(path: String): Flow<List<FileRepresentation>>
    fun observeType(type: FileType): Flow<List<FileRepresentation>>
    suspend fun get(path: String): FileRepresentation?
    suspend fun isExist(path: String): Boolean
    suspend fun getContent(path: String): List<FileRepresentation>
    suspend fun delete(file: FileRepresentation)
    suspend fun delete(path: String)
    suspend fun insert(file: FileRepresentation)
    suspend fun update(file: FileRepresentation)
}