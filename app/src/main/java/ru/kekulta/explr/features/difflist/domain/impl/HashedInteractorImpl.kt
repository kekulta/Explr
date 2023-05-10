package ru.kekulta.explr.features.difflist.domain.impl

import android.os.Environment
import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import ru.kekulta.explr.features.difflist.domain.api.HashedRepository
import ru.kekulta.explr.features.difflist.domain.api.usecases.HashedInteractor
import ru.kekulta.explr.features.difflist.domain.models.HashedFile
import ru.kekulta.explr.features.difflist.domain.models.enums.ChangeType
import ru.kekulta.explr.features.list.domain.api.usecases.FileUtil
import ru.kekulta.explr.features.list.domain.api.usecases.FilterManager
import ru.kekulta.explr.features.list.domain.models.states.FilterState
import ru.kekulta.explr.shared.utils.hashcode
import java.io.File

class HashedInteractorImpl(
    private val hashedRepository: HashedRepository,
    private val fileUtil: FileUtil,
    private val filterManager: FilterManager,
) : HashedInteractor {
    private fun Flow<List<HashedFile>>.filterHiddenNomedia(filter: Flow<FilterState>): Flow<List<HashedFile>> =
        filter.flatMapLatest { filterState ->
            map { list ->
                list.filter { file ->
                    (!file.hidden || filterState.showHidden) && (!file.nomedia || filterState.showNomedia)
                }
            }
        }

    override fun observeNew(): Flow<List<HashedFile>> =
        hashedRepository.observeNew().filterHiddenNomedia(filterManager.getFilterStateFlow())

    override fun observeOld(): Flow<List<HashedFile>> =
        hashedRepository.observeOld().filterHiddenNomedia(filterManager.getFilterStateFlow())

    override fun observeChanged(): Flow<List<HashedFile>> =
        hashedRepository.observeChanged().filterHiddenNomedia(filterManager.getFilterStateFlow())

    override fun observeDeleted(): Flow<List<HashedFile>> =
        hashedRepository.observeDeleted().filterHiddenNomedia(filterManager.getFilterStateFlow())

    override suspend fun updateStart() {
        hashedRepository.deleteAllType(ChangeType.DELETED)
        hashedRepository.setAllType(ChangeType.PROCESSING)
        updateStart(Environment.getExternalStorageDirectory(), false, false)
        hashedRepository.changeAllType(ChangeType.PROCESSING, ChangeType.DELETED)
    }

    private suspend fun updateStart(file: File, _hidden: Boolean, _nomedia: Boolean) {
        if (file.exists()) {
            val nomedia =
                _nomedia || (file.listFiles()?.any { it.name == FileUtil.NOMEDIA } ?: false)
            val hidden = _hidden || file.isHidden
            if (file.isDirectory) {
                file.listFiles()?.forEach { f -> updateStart(f, hidden, nomedia) }
            } else {
                val cache = hashedRepository.get(file.path)
                val hash = file.hashcode
                if (cache == null) {
                    hashedRepository.insert(
                        HashedFile(
                            path = file.path,
                            name = file.name,
                            type = fileUtil.getType(file),
                            hash = hash,
                            changeType = ChangeType.NEW,
                            hidden = hidden,
                            nomedia = nomedia,
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