package ru.kekulta.explr.shared.navigation.api

import ru.kekulta.explr.shared.navigation.models.Backstack

interface Router {
    fun navigate(command: Command)
    fun attachNavigator(navigator: Navigator, backstack: Backstack)
    fun detachNavigator(): Backstack
}