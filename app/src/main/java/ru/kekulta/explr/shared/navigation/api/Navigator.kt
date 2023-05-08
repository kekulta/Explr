package ru.kekulta.explr.shared.navigation.api

interface Navigator {
    fun performCommand(command: Command, noAnimation: Boolean = false)
}