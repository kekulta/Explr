package ru.kekulta.explr.features.main.domain.impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import ru.kekulta.explr.features.main.domain.api.ToolBarManager
import ru.kekulta.explr.features.main.domain.models.ToolBarState

class ToolBarManagerImpl(
    toolBarState: ToolBarState = ToolBarState()
) : ToolBarManager {

    override var toolBarState: ToolBarState = toolBarState
        set(value) {
            field = value
            toolBarStateFlow.value = value
        }

    private val toolBarStateFlow = MutableStateFlow(toolBarState)

    override fun getCurrentToolBarStateFlow(): Flow<ToolBarState> = toolBarStateFlow
}