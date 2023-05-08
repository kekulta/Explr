package ru.kekulta.explr.shared.navigation

import ru.kekulta.explr.shared.navigation.api.Command
import ru.kekulta.explr.shared.navigation.api.Navigator
import ru.kekulta.explr.shared.navigation.api.Router

class AppRouter() : Router {
    private var navigator: Navigator? = null


    override fun navigate(command: Command) {
        navigator?.performCommand(command, false)
    }

    override fun attachNavigator(navigator: Navigator) {
        this.navigator = navigator
    }

    override fun detachNavigator() {
        this.navigator = null
    }

}