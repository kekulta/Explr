package ru.kekulta.explr.shared.navigation.api

import ru.kekulta.explr.shared.navigation.models.Backstack

interface Navigator {
    fun performCommand(command: Command, backstack: Backstack): Backstack
}