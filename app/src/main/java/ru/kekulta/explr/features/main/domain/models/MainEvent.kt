package ru.kekulta.explr.features.main.domain.models

import ru.kekulta.explr.features.list.domain.models.enums.SortType

sealed class MainEvent {
    class ShowSortMenu(val state: SortType): MainEvent()
}
