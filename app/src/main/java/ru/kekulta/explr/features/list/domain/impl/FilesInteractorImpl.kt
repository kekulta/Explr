package ru.kekulta.explr.features.list.domain.impl

import android.app.Application
import android.os.Environment
import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import ru.kekulta.explr.di.MainServiceLocator
import ru.kekulta.explr.features.list.domain.api.FilesInteractor
import ru.kekulta.explr.features.list.domain.api.FilesRepository
import ru.kekulta.explr.features.list.domain.models.FileRepresentation
import ru.kekulta.explr.shared.utils.FileType
import ru.kekulta.explr.shared.utils.shortToast
import java.io.File

class FilesInteractorImpl(private val repository: FilesRepository) : FilesInteractor {
    init {
        runBlocking {
            launch {
                repository.insert(
                    FileRepresentation(
                        Environment.getExternalStorageDirectory(),
                        0
                    ).copy(parent = null)
                )
            }
        }
    }

    override fun observeContent(path: String): Flow<List<FileRepresentation>> = when {
        path.startsWith(FilesInteractor.DOWNLOADS_CATEGORY) -> repository.observeContent(Environment.getExternalStorageDirectory().path + File.separator + DOWNLOADS_DIRECTORY)
        path.startsWith(FilesInteractor.RECENT_CATEGORY) -> repository.observeRecent()
        path.startsWith(FilesInteractor.AUDIO_CATEGORY) -> repository.observeType(FileType.AUDIO)
        path.startsWith(FilesInteractor.VIDEOS_CATEGORY) -> repository.observeType(FileType.VIDEO)
        path.startsWith(FilesInteractor.DOCUMENTS_CATEGORY) -> repository.observeType(FileType.DOCUMENT)
        path.startsWith(FilesInteractor.IMAGES_CATEGORY) -> repository.observeType(FileType.IMAGE)
        path.startsWith(FilesInteractor.STORAGE_CATEGORY) -> repository.observeContent(
            path.replace(
                FilesInteractor.STORAGE_CATEGORY,
                Environment.getExternalStorageDirectory().path
            )
        )

        else -> repository.observeContent(path)
    }

    suspend fun update(_path: String) {
        val path =
            if (File(_path).exists()) _path else Environment.getExternalStorageDirectory().path
        val file = File(path)
        if (file.isDirectory) {

            Log.d(LOG_TAG, "updating: $path")

            val parent = repository.get(path)

            if (file.exists() && parent != null) {
                val filesDb = repository.getContent(path)
                val files =
                    (file.listFiles() ?: arrayOf()).map {
                        FileRepresentation(
                            it,
                            parent.level + 1
                        )
                    }
                files.forEach {
                    repository.update(it)
                    repository.insert(it)
                }
                filesDb.filterNot { it in files }.forEach { repository.delete(it.path) }
                file.listFiles()?.filter { it.isDirectory }?.forEach { f: File -> update(f.path) }
            } else {
                repository.delete(path)
            }
        }
    }

    companion object {
        const val LOG_TAG = "FilesInteractorImpl"
        private const val DOWNLOADS_DIRECTORY = "Download"
    }
}