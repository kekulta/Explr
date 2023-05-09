package ru.kekulta.explr.features.list.domain.api.usecases

import kotlinx.coroutines.flow.Flow
import ru.kekulta.explr.features.list.domain.models.states.FilterState

interface FilterManager {
    var filterState: FilterState
    fun getFilterStateFlow(): Flow<FilterState>
}