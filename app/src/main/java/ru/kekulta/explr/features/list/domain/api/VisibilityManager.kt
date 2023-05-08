package ru.kekulta.explr.features.list.domain.api

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import ru.kekulta.explr.features.list.data.FilesRepositoryImpl
import ru.kekulta.explr.features.list.domain.models.FileRepresentation

interface VisibilityManager {
    var showHidden: Boolean
    var showNomedia: Boolean
    fun getHiddenStatus(): Flow<Boolean>
    fun getNomediaStatus(): Flow<Boolean>

    fun <T> observeHiddenNomedia(transform: (Boolean, Boolean) -> Flow<T>): Flow<T> = getHiddenStatus()
        .combine(getNomediaStatus()) { hidden, nomedia -> Pair(hidden, nomedia) }.flatMapLatest { (hidden, nomedia) -> transform.invoke(hidden, nomedia) }
}