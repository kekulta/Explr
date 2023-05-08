package ru.kekulta.explr.features.list.domain.impl

import android.os.Environment
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import ru.kekulta.explr.features.list.domain.api.FilesInteractor
import ru.kekulta.explr.features.list.domain.api.FilesInteractorResponse
import ru.kekulta.explr.features.list.domain.api.FilesRepository
import ru.kekulta.explr.features.list.domain.models.FileRepresentation
import java.io.File
import kotlin.concurrent.thread

class FilesInteractorImpl(private val repository: FilesRepository) : FilesInteractor {
    init {
        runBlocking {
            launch {
                repository.insert(
                    FileRepresentation(
                        Environment.getExternalStorageDirectory(),
                        0
                    ).copy(parent = FileRepresentation.ROOT)
                )
            }
        }
    }

    override fun observeContent(path: String): Flow<List<FileRepresentation>> = repository.observeContent(path)

    suspend fun update(path: String) {

        //Log.d(LOG_TAG, "updating: $path, thread: ${Thread.currentThread()}")
        val parent = repository.get(path)
        val file = File(path)

        if (file.isDirectory) {
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
    }
}