package ru.kekulta.explr.shared.navigation

import ru.kekulta.explr.shared.navigation.api.Command
import ru.kekulta.explr.shared.navigation.api.Navigator
import ru.kekulta.explr.shared.navigation.api.Router
import ru.kekulta.explr.shared.navigation.models.Backstack

class AppRouter() : Router {
    private var navigator: Navigator? = null
    private var backstack = Backstack()

    override fun navigate(command: Command) {
        navigator?.performCommand(command, backstack)?.let { backstack = it }
    }

    override fun attachNavigator(navigator: Navigator, backstack: Backstack) {
        this.navigator = navigator
        this.backstack = backstack
    }

    override fun detachNavigator(): Backstack {
        this.navigator = null
        return backstack
    }

}