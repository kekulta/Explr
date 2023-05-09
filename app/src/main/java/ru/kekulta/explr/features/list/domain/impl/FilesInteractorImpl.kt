package ru.kekulta.explr.features.list.domain.impl

import android.os.Environment
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import ru.kekulta.explr.features.list.domain.api.FilesInteractor
import ru.kekulta.explr.features.list.domain.api.FilesRepository
import ru.kekulta.explr.features.list.domain.api.SortingManager
import ru.kekulta.explr.features.list.domain.api.FilterManager
import ru.kekulta.explr.features.list.domain.api.FileUtil
import ru.kekulta.explr.features.list.domain.models.Category
import ru.kekulta.explr.features.list.domain.models.FileRepresentation
import ru.kekulta.explr.features.list.domain.models.SortType
import ru.kekulta.explr.shared.utils.FileType
import ru.kekulta.explr.shared.utils.extension
import ru.kekulta.explr.shared.utils.size
import java.io.File

class FilesInteractorImpl(
    private val repository: FilesRepository,
    private val filterManager: FilterManager,
    private val sortingManager: SortingManager,
    private val typeChecker: FileUtil,
) : FilesInteractor {
    init {
        runBlocking {
            launch {
                repository.insert(
                    FileRepresentation(
                        file = Environment.getExternalStorageDirectory(),
                        level = 0,
                        isHidden = false,
                        isNoMedia = false,
                        type = FileType.DIRECTORY
                    ).copy(parent = null)
                )
            }
        }
    }

    private fun sortContent(
        list: Flow<List<FileRepresentation>>,
        sortType: SortType,
        reversed: Boolean
    ): Flow<List<FileRepresentation>> = when (sortType) {
        SortType.NAME -> list.map { it.sortedBy { file -> file.name } }
        SortType.NO_SORT -> list.map { it.sortedBy { file -> file.name } }
        SortType.DATE_MODIFIED -> list.map { it.sortedBy { file -> file.lastModified } }
        SortType.SIZE -> list.map { it.sortedBy { file -> file.size } }
        SortType.TYPE -> list.map {
            it.sortedWith(
                compareBy(
                    { file -> file.type },
                    { file -> file.extension })
            )
        }
    }.map { listSorted -> if (reversed) listSorted.reversed() else listSorted }


    override fun observeContent(path: String): Flow<List<FileRepresentation>> =
        sortingManager.observeSort { sortType, reversed ->
            sortContent(
                observeRawContent(path), sortType, reversed
            )
        }

    private fun observeRawContent(path: String): Flow<List<FileRepresentation>> = when {
        path.startsWith(Category.DOWNLOADS.path) -> filterManager.getFilterStateFlow()
            .flatMapLatest { filterState ->
                repository.observeContent(
                    Environment.getExternalStorageDirectory().path + File.separator + DOWNLOADS_DIRECTORY,
                    filterState.showHidden,
                    filterState.showNomedia
                )
            }

        path.startsWith(Category.RECENT.path) -> filterManager.getFilterStateFlow()
            .flatMapLatest { filterState ->
                repository.observeRecent(
                    filterState.showHidden,
                    filterState.showNomedia
                )
            }

        path.startsWith(Category.AUDIO.path) -> filterManager.getFilterStateFlow()
            .flatMapLatest { filterState ->
                repository.observeType(
                    FileType.AUDIO,
                    filterState.showHidden,
                    filterState.showNomedia
                )
            }

        path.startsWith(Category.VIDEOS.path) -> filterManager.getFilterStateFlow()
            .flatMapLatest { filterState ->
                repository.observeType(
                    FileType.VIDEO,
                    filterState.showHidden,
                    filterState.showNomedia
                )
            }

        path.startsWith(Category.DOCUMENTS.path) -> filterManager.getFilterStateFlow()
            .flatMapLatest { filterState ->
                repository.observeType(
                    FileType.DOCUMENT,
                    filterState.showHidden,
                    filterState.showNomedia
                )
            }

        path.startsWith(Category.IMAGES.path) -> filterManager.getFilterStateFlow()
            .flatMapLatest { filterState ->
                repository.observeType(
                    FileType.IMAGE,
                    filterState.showHidden,
                    filterState.showNomedia
                )
            }

        path.startsWith(Category.STORAGE.path) -> filterManager.getFilterStateFlow()
            .flatMapLatest { filterState ->
                repository.observeContent(
                    path.replace(
                        Category.STORAGE.path,
                        Environment.getExternalStorageDirectory().path
                    ), filterState.showHidden, true
                )
            }

        else -> filterManager.getFilterStateFlow()
            .flatMapLatest { filterState ->
                repository.observeContent(
                    path,
                    filterState.showHidden,
                    true
                )
            }
    }

    //TODO return to single function
    suspend fun update(_path: String): Double {
        val path =
            if (File(_path).exists()) _path else Environment.getExternalStorageDirectory().path
        return repository.get(path)?.let {
            recursiveUpdate(path, it.isHidden, it.isNoMedia)
        } ?: 0.0
    }

    private suspend fun recursiveUpdate(
        path: String,
        _isHidden: Boolean,
        _isNoMedia: Boolean
    ): Double {
        val file = File(path)
        return if (file.isDirectory) {

            val parent = repository.get(path)

            if (file.exists() && parent != null) {
                val filesDb = repository.getContent(path)
                val isNoMedia =
                    _isNoMedia || (file.listFiles() ?: arrayOf()).any { it.name == NOMEDIA }
                val isHidden = _isHidden || file.isHidden
                val files =
                    (file.listFiles() ?: arrayOf()).map {
                        FileRepresentation(
                            file = it,
                            level = parent.level + 1,
                            isHidden = it.isHidden || isHidden,
                            isNoMedia = isNoMedia,
                            type = typeChecker.getType(it),
                        )
                    }
                val filesSet = files.map { it.path }.toSet()
                files.forEach {
                    repository.update(it.copy(size = repository.get(it.path)?.size ?: 0.0))
                    repository.insert(it)
                }
                filesDb.filterNot { it.path in filesSet }.forEach { repository.delete(it.path) }
                val size =
                    (file.listFiles()?.sumOf { f: File -> update(f.path) } ?: 0.0) + file.size
                repository.updateSize(path, size)
                size
            } else {
                repository.delete(path)
                0.0
            }
        } else {
            file.size
        }
    }

    companion object {
        const val LOG_TAG = "FilesInteractorImpl"
        private const val DOWNLOADS_DIRECTORY = "Download"
        private const val NOMEDIA = ".nomedia"
    }
}