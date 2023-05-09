package ru.kekulta.explr.features.main.domain.models

import ru.kekulta.explr.features.list.domain.models.states.FilterState

data class MainState(val filterState: FilterState, val toolBarState: ToolBarState)