package ru.kekulta.explr.shared.navigation.api

import android.os.Bundle
import ru.kekulta.explr.shared.navigation.models.Destination
import kotlin.random.Random

sealed interface Command {

    class ForwardTo(val destination: Destination, val id: Int) : Command {
        constructor(
            destinationKey: String,
            args: Bundle? = null,
            id: Int = random.nextInt()
        ) : this(
            Destination(destinationKey, args), id
        )
    }

    class ReplaceTo(val destination: Destination, val id: Int) : Command {
        constructor(
            destinationKey: String,
            args: Bundle? = null,
            id: Int = random.nextInt()
        ) : this(
            Destination(destinationKey, args), id
        )
    }

    class ReturnTo(val id: Int) : Command

    object Back : Command

    companion object {
        private val random = Random(System.currentTimeMillis())
    }
}