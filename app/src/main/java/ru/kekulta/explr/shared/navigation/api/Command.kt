package ru.kekulta.explr.shared.navigation.api

import android.os.Bundle

sealed interface Command {

    class ForwardTo(val destination: String, val args: Bundle? = null) : Command
    class ReplaceTo(val destination: String, val args: Bundle? = null) : Command
    object Back : Command
}