package ru.kekulta.explr.features.list.domain.api

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import ru.kekulta.explr.features.list.domain.models.FilterState

interface FilterManager {
    var filterState: FilterState
    fun getFilterStateFlow(): Flow<FilterState>
}