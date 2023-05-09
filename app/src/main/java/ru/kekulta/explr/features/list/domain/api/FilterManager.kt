package ru.kekulta.explr.features.list.domain.api

import kotlinx.coroutines.flow.Flow
import ru.kekulta.explr.features.list.domain.models.FilterState

interface FilterManager {
    var filterState: FilterState
    fun getFilterStateFlow(): Flow<FilterState>
}