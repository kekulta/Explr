package ru.kekulta.explr.features.main.domain.api

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import ru.kekulta.explr.features.main.domain.models.ToolBarState


interface ToolBarManager {
    var toolBarState: ToolBarState
    fun getCurrentToolBarStateFlow(): Flow<ToolBarState>
}