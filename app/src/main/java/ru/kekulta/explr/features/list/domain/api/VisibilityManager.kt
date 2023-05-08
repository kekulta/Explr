package ru.kekulta.explr.features.list.domain.api

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface VisibilityManager {
    var showHidden: Boolean
    var showNomedia: Boolean
    fun getHiddenStatus(): Flow<Boolean>
    fun getNomediaStatus(): Flow<Boolean>
}