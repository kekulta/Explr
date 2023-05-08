package ru.kekulta.explr.features.list.domain.api

import kotlinx.coroutines.flow.Flow
import ru.kekulta.explr.features.list.domain.models.FileRepresentation

sealed class FilesInteractorResponse {
    class Success(val data: Flow<List<FileRepresentation>>) : FilesInteractorResponse()
    object Error : FilesInteractorResponse()
}