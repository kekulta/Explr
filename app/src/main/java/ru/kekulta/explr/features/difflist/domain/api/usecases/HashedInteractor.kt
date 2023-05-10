package ru.kekulta.explr.features.difflist.domain.api.usecases

import kotlinx.coroutines.flow.Flow
import ru.kekulta.explr.features.difflist.domain.models.HashedFile

interface HashedInteractor {
    fun observeNew(): Flow<List<HashedFile>>
    fun observeOld(): Flow<List<HashedFile>>
    fun observeChanged(): Flow<List<HashedFile>>
    fun observeDeleted(): Flow<List<HashedFile>>
    suspend fun updateStart()
    suspend fun updateCurrent()
}