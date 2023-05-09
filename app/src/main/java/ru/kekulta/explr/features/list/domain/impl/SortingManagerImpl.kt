package ru.kekulta.explr.features.list.domain.impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import ru.kekulta.explr.features.list.domain.api.SortingManager
import ru.kekulta.explr.features.list.domain.models.SortType

class SortingManagerImpl(type: SortType = SortType.NAME, reversed: Boolean = false) :
    SortingManager {
    override var type: SortType = type
        set(value) {
            field = value
            typeFlow.value = value
        }
    override var reversed: Boolean = reversed
        set(value) {
            field = value
            reversedFlow.value = value
        }

    private val typeFlow = MutableStateFlow(type)
    private val reversedFlow = MutableStateFlow(reversed)

    override fun getTypeFlow(): Flow<SortType> = typeFlow

    override fun getReversedFlow(): Flow<Boolean> = reversedFlow
}