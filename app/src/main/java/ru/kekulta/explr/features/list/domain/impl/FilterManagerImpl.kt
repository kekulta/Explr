package ru.kekulta.explr.features.list.domain.impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import ru.kekulta.explr.features.list.domain.api.FilterManager
import ru.kekulta.explr.features.list.domain.models.FilterState

class FilterManagerImpl(filterState: FilterState = FilterState()) :
    FilterManager {

    override var filterState: FilterState = filterState
        set(value) {
            field = value
            filterStateFlow.value = value
        }


    private val filterStateFlow = MutableStateFlow(filterState)
    override fun getFilterStateFlow(): Flow<FilterState> = filterStateFlow

    companion object {
        const val LOG_TAG = "FilterManagerImpl"
    }
}