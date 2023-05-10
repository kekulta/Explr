package ru.kekulta.explr.features.difflist.domain.impl

import android.os.Environment
import android.util.Log
import kotlinx.coroutines.flow.Flow
import ru.kekulta.explr.features.difflist.domain.api.HashedRepository
import ru.kekulta.explr.features.difflist.domain.api.usecases.HashedInteractor
import ru.kekulta.explr.features.difflist.domain.models.HashedFile
import ru.kekulta.explr.features.difflist.domain.models.enums.ChangeType
import ru.kekulta.explr.features.list.domain.api.usecases.FileUtil
import ru.kekulta.explr.shared.utils.hashcode
import java.io.File

class HashedInteractorImpl(
    private val hashedRepository: HashedRepository,
    private val fileUtil: FileUtil
) : HashedInteractor {
    override fun observeNew(): Flow<List<HashedFile>> = hashedRepository.observeNew()

    override fun observeOld(): Flow<List<HashedFile>> = hashedRepository.observeOld()

    override fun observeChanged(): Flow<List<HashedFile>> = hashedRepository.observeChanged()

    override fun observeDeleted(): Flow<List<HashedFile>> = hashedRepository.observeDeleted()

    override suspend fun updateStart() {
        Log.d(LOG_TAG, "Starting updates")
        hashedRepository.deleteAllType(ChangeType.DELETED)
        hashedRepository.setAllType(ChangeType.PROCESSING)
        updateStart(Environment.getExternalStorageDirectory())
        hashedRepository.changeAllType(ChangeType.PROCESSING, ChangeType.DELETED)
    }

    private suspend fun updateStart(file: File) {
        if (file.exists()) {
            if (file.isDirectory) {
                file.listFiles()?.forEach { f -> updateStart(f) }
            } else {
                val cache = hashedRepository.get(file.path)
                Log.d(LOG_TAG, "Checking: ${file.path}, cached: ${cache != null}")
                val hash = file.hashcode
                if (cache == null) {
                    hashedRepository.insert(
                        HashedFile(
                            file.path,
                            file.name,
                            fileUtil.getType(file),
                            hash,
                            ChangeType.NEW
                        )
                    )
                } else {
                    if (cache.hash == hash) {
                        hashedRepository.update(cache.copy(changeType = ChangeType.OLD))
                    } else {
                        hashedRepository.update(cache.copy(changeType = ChangeType.CHANGED))
                    }
                }
            }
        }
    }

    override suspend fun updateCurrent() {
        TODO("Not yet implemented")
    }

    companion object {
        const val LOG_TAG = "HashedInteractorImpl"
    }
}