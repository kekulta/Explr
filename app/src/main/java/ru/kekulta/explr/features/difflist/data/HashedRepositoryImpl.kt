package ru.kekulta.explr.features.difflist.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.kekulta.explr.features.difflist.data.database.dao.HashedDao
import ru.kekulta.explr.features.difflist.domain.api.HashedRepository
import ru.kekulta.explr.features.difflist.domain.models.HashedFile
import ru.kekulta.explr.features.difflist.domain.models.enums.ChangeType

class HashedRepositoryImpl(private val dao: HashedDao) : HashedRepository {
    override fun observeNew(): Flow<List<HashedFile>> =
        dao.observeType(ChangeType.NEW).map { list -> list.map { file -> HashedFile(file) } }

    override fun observeOld(): Flow<List<HashedFile>> =
        dao.observeType(ChangeType.OLD).map { list -> list.map { file -> HashedFile(file) } }

    override fun observeChanged(): Flow<List<HashedFile>> =
        dao.observeType(ChangeType.CHANGED).map { list -> list.map { file -> HashedFile(file) } }

    override fun observeDeleted(): Flow<List<HashedFile>> =
        dao.observeType(ChangeType.DELETED).map { list -> list.map { file -> HashedFile(file) } }

    override suspend fun get(path: String): HashedFile? =
        dao.get(path)?.let { dto -> HashedFile(dto) }

    override suspend fun isExist(path: String): Boolean = dao.isExist(path)

    override suspend fun delete(file: HashedFile) = dao.delete(file.path)

    override suspend fun delete(path: String) = dao.delete(path)

    override suspend fun insert(file: HashedFile) = dao.insert(file.toDto())

    override suspend fun update(file: HashedFile) = dao.update(file.toDto())

    override suspend fun setAllType(type: ChangeType) = dao.setAllType(type)

    override suspend fun deleteAllType(type: ChangeType) = dao.deleteAllType(type)
    override suspend fun changeAllType(from: ChangeType, to: ChangeType) =
        dao.changeAllType(from, to)
}