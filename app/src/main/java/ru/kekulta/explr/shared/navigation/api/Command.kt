package ru.kekulta.explr.shared.navigation.api

import android.os.Bundle
import ru.kekulta.explr.shared.navigation.models.Destination

sealed interface Command {
    class ForwardTo(val destination: Destination) : Command {
        constructor(destinationKey: String, args: Bundle? = null) : this(
            Destination(destinationKey, args)
        )
    }

    class ReplaceTo(val destination: Destination) : Command {
        constructor(destinationKey: String, args: Bundle? = null) : this(
            Destination(destinationKey, args)
        )
    }

    object Back : Command
}