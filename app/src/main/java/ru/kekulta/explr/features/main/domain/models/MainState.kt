package ru.kekulta.explr.features.main.domain.models

import ru.kekulta.explr.features.list.domain.models.Category
import ru.kekulta.explr.features.list.domain.models.FilterState

data class MainState(val category: Category, val filterState: FilterState, val toolBarState: ToolBarState)