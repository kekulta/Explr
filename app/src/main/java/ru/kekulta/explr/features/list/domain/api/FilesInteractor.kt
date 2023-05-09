package ru.kekulta.explr.features.list.domain.api

import android.os.Environment
import kotlinx.coroutines.flow.Flow
import ru.kekulta.explr.features.list.domain.models.FileRepresentation

interface FilesInteractor {
    fun observeContent(path: String): Flow<List<FileRepresentation>>

}