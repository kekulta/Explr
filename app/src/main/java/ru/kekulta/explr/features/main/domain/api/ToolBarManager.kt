package ru.kekulta.explr.features.main.domain.api

import kotlinx.coroutines.flow.Flow
import ru.kekulta.explr.features.main.domain.models.ToolBarState


interface ToolBarManager {
    var toolBarState: ToolBarState
    fun getCurrentToolBarStateFlow(): Flow<ToolBarState>
}