package ru.kekulta.explr.features.main.domain

import android.app.Activity
import android.os.Bundle
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import ru.kekulta.explr.features.difflist.ui.HashedListFragment
import ru.kekulta.explr.features.list.ui.FilesListFragment
import ru.kekulta.explr.features.main.ui.PermissionsDeniedFragment
import ru.kekulta.explr.features.main.ui.PermissionsRequestFragment
import ru.kekulta.explr.shared.navigation.api.Command
import ru.kekulta.explr.shared.navigation.api.Navigator

class MainNavigator(
    private val activity: Activity,
    private val fragmentManager: FragmentManager,
    @IdRes private val container: Int
) : Navigator {
    //TODO fix/add backstack

    override fun performCommand(
        command: Command,
        noAnimation: Boolean,
    ) {
        when (command) {
            is Command.ForwardTo -> {
                fragmentManager.commit {
                    setReorderingAllowed(true)
                    replace(container, provideFragment(command.destination, command.args))
                    addToBackStack(null)
                }
            }

            is Command.ReplaceTo -> {

                fragmentManager.popBackStack()
                fragmentManager.commit {
                    setReorderingAllowed(true)
                    replace(container, provideFragment(command.destination, command.args))
                    addToBackStack(null)
                }

            }

            is Command.Back -> {
                fragmentManager.popBackStack()
                if (fragmentManager.backStackEntryCount <= 1) {
                    activity.finish()
                }
            }
        }
    }

    private fun provideFragment(destination: String, args: Bundle?): Fragment {
        return when (destination) {
            PermissionsRequestFragment.DESTINATION_KEY -> PermissionsRequestFragment().apply {
                arguments = args
            }

            PermissionsDeniedFragment.DESTINATION_KEY -> PermissionsDeniedFragment().apply {
                arguments = args
            }

            FilesListFragment.DESTINATION_KEY -> FilesListFragment().apply {
                arguments = args
            }

            HashedListFragment.DESTINATION_KEY -> HashedListFragment().apply {
                arguments = args
            }

            else -> throw IllegalArgumentException("Invalid screen")
        }
    }

    companion object {
        const val LOG_TAG = "MainNavigator"
    }
}
