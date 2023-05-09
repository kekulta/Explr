package ru.kekulta.explr.features.main.domain.models

data class MainState(val drawerItem: Int, val hidden: Boolean, val nomedia: Boolean, val toolBarState: ToolBarState)