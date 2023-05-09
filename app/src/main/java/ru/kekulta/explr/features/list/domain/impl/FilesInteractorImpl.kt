package ru.kekulta.explr.features.list.domain.impl

import android.app.Application
import android.os.Environment
import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import ru.kekulta.explr.di.MainServiceLocator
import ru.kekulta.explr.features.list.domain.api.FilesInteractor
import ru.kekulta.explr.features.list.domain.api.FilesRepository
import ru.kekulta.explr.features.list.domain.api.SortingManager
import ru.kekulta.explr.features.list.domain.api.VisibilityManager
import ru.kekulta.explr.features.list.domain.models.FileRepresentation
import ru.kekulta.explr.features.list.domain.models.SortType
import ru.kekulta.explr.shared.utils.FileType
import ru.kekulta.explr.shared.utils.extension
import ru.kekulta.explr.shared.utils.shortToast
import ru.kekulta.explr.shared.utils.size
import java.io.File

class FilesInteractorImpl(
    private val repository: FilesRepository,
    private val visibilityManager: VisibilityManager,
    private val sortingManager: SortingManager,
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
        path.startsWith(FilesInteractor.DOWNLOADS_CATEGORY) -> visibilityManager.observeHiddenNomedia { hidden, nomedia ->
            repository.observeContent(
                Environment.getExternalStorageDirectory().path + File.separator + DOWNLOADS_DIRECTORY,
                hidden,
                nomedia
            )
        }

        path.startsWith(FilesInteractor.RECENT_CATEGORY) -> visibilityManager.observeHiddenNomedia { hidden, nomedia ->
            repository.observeRecent(
                hidden,
                nomedia
            )
        }

        path.startsWith(FilesInteractor.AUDIO_CATEGORY) -> visibilityManager.observeHiddenNomedia { hidden, nomedia ->
            repository.observeType(
                FileType.AUDIO,
                hidden,
                nomedia
            )
        }

        path.startsWith(FilesInteractor.VIDEOS_CATEGORY) -> visibilityManager.observeHiddenNomedia { hidden, nomedia ->
            repository.observeType(
                FileType.VIDEO,
                hidden,
                nomedia
            )
        }

        path.startsWith(FilesInteractor.DOCUMENTS_CATEGORY) -> visibilityManager.observeHiddenNomedia { hidden, nomedia ->
            repository.observeType(
                FileType.DOCUMENT,
                hidden,
                nomedia
            )
        }

        path.startsWith(FilesInteractor.IMAGES_CATEGORY) -> visibilityManager.observeHiddenNomedia { hidden, nomedia ->
            repository.observeType(
                FileType.IMAGE,
                hidden,
                nomedia
            )
        }

        path.startsWith(FilesInteractor.STORAGE_CATEGORY) -> visibilityManager.observeHiddenNomedia { hidden, _ ->
            repository.observeContent(
                path.replace(
                    FilesInteractor.STORAGE_CATEGORY,
                    Environment.getExternalStorageDirectory().path
                ), hidden, true
            )
        }

        else -> visibilityManager.observeHiddenNomedia { hidden, _ ->
            repository.observeContent(
                path,
                hidden,
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
                            isNoMedia = isNoMedia
                        )
                    }
                val filesSet = files.map { it.path }.toSet()
                files.forEach {
                    repository.update(it)
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