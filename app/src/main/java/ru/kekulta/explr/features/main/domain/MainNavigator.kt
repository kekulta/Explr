package ru.kekulta.explr.features.main.domain

import android.app.Activity
import android.os.Bundle
import android.util.Log
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
import ru.kekulta.explr.shared.navigation.models.Backstack
import ru.kekulta.explr.shared.navigation.models.Transaction
import ru.kekulta.explr.shared.navigation.models.addToBackStack
import ru.kekulta.explr.shared.navigation.models.lastDestination
import ru.kekulta.explr.shared.navigation.models.popBackStack
import kotlin.random.Random

class MainNavigator(
    private val activity: Activity,
    private val fragmentManager: FragmentManager,
    @IdRes private val container: Int
) : Navigator {
    //TODO fix/add backstack
    val random = Random(System.currentTimeMillis())

    override fun performCommand(
        command: Command,
        backstack: Backstack,
    ): Backstack {
        Log.d(LOG_TAG, "backstack: ${backstack.stack}")
        return when (command) {
            is Command.ForwardTo -> {
                fragmentManager.commit {
                    setReorderingAllowed(true)
                    replace(
                        container, provideFragment(
                            command.destination.destinationKey, command.destination.args
                        )
                    )
                }
                backstack.addToBackStack(Transaction(random.nextInt(), command.destination))
            }

            is Command.ReplaceTo -> {
                fragmentManager.commit {
                    setReorderingAllowed(true)
                    replace(
                        container, provideFragment(
                            command.destination.destinationKey, command.destination.args
                        )
                    )
                }
                backstack.popBackStack()
                    .addToBackStack(Transaction(random.nextInt(), command.destination))
            }

            is Command.Back -> {
                if (backstack.stack.size <= 1) {
                    activity.finish()
                    Backstack()
                } else {
                    backstack.popBackStack().apply {
                        fragmentManager.commit {
                            setReorderingAllowed(true)
                            replace(
                                container, provideFragment(
                                    lastDestination.destinationKey, lastDestination.args
                                )
                            )
                        }
                    }
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
