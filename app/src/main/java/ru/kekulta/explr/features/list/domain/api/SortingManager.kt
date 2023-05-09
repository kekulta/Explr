package ru.kekulta.explr.features.list.domain.api

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import ru.kekulta.explr.features.list.domain.models.SortType

interface SortingManager {
    var type: SortType
    var reversed: Boolean

    fun getTypeFlow(): Flow<SortType>
    fun getReversedFlow(): Flow<Boolean>

    fun <T> observeSort(transform: (SortType, Boolean) -> Flow<T>) =
        getTypeFlow().combine(getReversedFlow()) { type, reversed -> Pair(type, reversed) }
            .flatMapLatest { transform.invoke(it.first, it.second) }
}
