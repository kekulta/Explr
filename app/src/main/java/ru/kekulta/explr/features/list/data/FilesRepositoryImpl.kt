package ru.kekulta.explr.features.list.data

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import ru.kekulta.explr.features.list.data.database.dao.FileDao
import ru.kekulta.explr.features.list.domain.api.FilesRepository
import ru.kekulta.explr.features.list.domain.api.VisibilityManager
import ru.kekulta.explr.features.list.domain.models.FileRepresentation
import ru.kekulta.explr.shared.utils.FileType

class FilesRepositoryImpl(
    private val dao: FileDao
) : FilesRepository {

    //TODO adjust flows
    override fun observeContent(
        path: String,
        hidden: Boolean,
        nomedia: Boolean
    ): Flow<List<FileRepresentation>> =
        dao.observeContent(path, hidden, nomedia)
            .map { list -> list.map { dto -> FileRepresentation(dto) } }


    override fun observeType(
        type: FileType,
        hidden: Boolean,
        nomedia: Boolean
    ): Flow<List<FileRepresentation>> =
        dao.observeType(type, hidden, nomedia)
            .map { list -> list.map { dto -> FileRepresentation(dto) } }


    override fun observeRecent(hidden: Boolean, nomedia: Boolean): Flow<List<FileRepresentation>> =
        dao.observeRecent(
            System.currentTimeMillis() - 24L * 60 * 60 * 1000, hidden, nomedia
        ).map { list -> list.map { dto -> FileRepresentation(dto) } }


    override suspend fun get(path: String): FileRepresentation? =
        dao.get(path)?.let { FileRepresentation(it) }

    override suspend fun isExist(path: String): Boolean = dao.isExist(path)


    override suspend fun getContent(path: String): List<FileRepresentation> =
        dao.getContent(path).map { FileRepresentation(it) }

    override suspend fun delete(file: FileRepresentation) {
        dao.delete(file.path)
    }

    override suspend fun delete(path: String) {
        dao.delete(path)
    }

    override suspend fun insert(file: FileRepresentation) {
        dao.insert(file.toDto())
    }

    override suspend fun update(file: FileRepresentation) {
        dao.update(file.toDto())
    }

    companion object {
        const val LOG_TAG = "FilesRepositoryImpl"
    }
}