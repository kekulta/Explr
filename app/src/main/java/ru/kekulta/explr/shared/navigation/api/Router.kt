package ru.kekulta.explr.shared.navigation.api

interface Router {
    fun navigate(command: Command)
    fun attachNavigator(navigator: Navigator)
    fun detachNavigator()
}