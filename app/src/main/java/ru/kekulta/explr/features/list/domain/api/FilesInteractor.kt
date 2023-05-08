package ru.kekulta.explr.features.list.domain.api

import android.os.Environment
import kotlinx.coroutines.flow.Flow
import ru.kekulta.explr.features.list.domain.models.FileRepresentation

interface FilesInteractor {
    fun observeContent(path: String): Flow<List<FileRepresentation>>

    // TODO make more consistent
    companion object {
        const val VIDEOS_CATEGORY = "VIDEOS::"
        const val AUDIO_CATEGORY = "AUDIO::"
        const val DOCUMENTS_CATEGORY = "DOCUMENTS::"
        const val IMAGES_CATEGORY = "IMAGES::"
        const val STORAGE_CATEGORY = "STORAGE::"
        const val DOWNLOADS_CATEGORY = "DOWNLOADS::"
        const val RECENT_CATEGORY = "RECENT::"
    }
}