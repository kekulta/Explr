package ru.kekulta.explr.features.main.domain

import android.app.Activity
import android.os.Bundle
import android.util.Log
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import ru.kekulta.explr.R
import ru.kekulta.explr.features.difflist.ui.HashedListFragment
import ru.kekulta.explr.features.list.ui.FilesListFragment
import ru.kekulta.explr.features.main.ui.PermissionsDeniedFragment
import ru.kekulta.explr.features.main.ui.PermissionsRequestFragment
import ru.kekulta.explr.shared.navigation.api.Command
import ru.kekulta.explr.shared.navigation.api.Navigator
import ru.kekulta.explr.shared.navigation.models.Backstack
import ru.kekulta.explr.shared.navigation.models.Destination
import ru.kekulta.explr.shared.navigation.models.Transaction
import ru.kekulta.explr.shared.navigation.models.addToBackStack
import ru.kekulta.explr.shared.navigation.models.dropAfter
import ru.kekulta.explr.shared.navigation.models.lastDestination
import ru.kekulta.explr.shared.navigation.models.popBackStack
import java.lang.ref.WeakReference
import kotlin.random.Random

class MainNavigator(
    private val activity: Activity,
    private val fragmentManager: FragmentManager,
    @IdRes private val container: Int
) : Navigator {
    val random = Random(System.currentTimeMillis())

    override fun performCommand(
        command: Command,
        backstack: Backstack,
    ): Backstack {
        Log.d(LOG_TAG, "backstack: ${backstack.stack}")
        return when (command) {
            is Command.ForwardTo -> {
                if (command.destination != backstack.lastDestination) {
                    var back = backstack
                    fragmentManager.commit {
                        setReorderingAllowed(true)
                        setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left)
                        replace(
                            container, provideFragment(
                                command.destination.destinationKey, command.destination.args
                            ).also {
                                back = backstack.addToBackStack(
                                    Transaction(
                                        command.id,
                                        command.destination,
                                        WeakReference(it)
                                    )
                                )
                            }
                        )
                    }
                    back
                } else {
                    backstack
                }
            }

            is Command.ReplaceTo -> {
                if (command.destination != backstack.lastDestination) {
                    var back = backstack
                    fragmentManager.commit {
                        setReorderingAllowed(true)
                        setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left)
                        replace(
                            container, provideFragment(
                                command.destination.destinationKey, command.destination.args
                            ).also {
                                back = backstack.popBackStack()
                                    .addToBackStack(
                                        Transaction(
                                            random.nextInt(),
                                            command.destination,
                                            WeakReference(it)
                                        )
                                    )
                            }
                        )
                    }
                    back
                } else {
                    backstack
                }
            }

            is Command.Back -> {
                if (backstack.stack.size <= 1) {
                    activity.finish()
                    Backstack()
                } else {
                    backstack.popBackStack().apply {
                        fragmentManager.commit {
                            setReorderingAllowed(true)
                            setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right)
                            replace(
                                container,
                                stack.last().cache.get() ?: provideFragment(
                                    lastDestination!!.destinationKey,
                                    lastDestination!!.args
                                )
                            )
                        }
                    }
                }
            }

            is Command.ReturnTo -> {
                val index = backstack.stack.indexOfFirst { it.id == command.id }
                Log.d(LOG_TAG, "$index")
                if (index == -1) {
                    backstack
                } else {
                    backstack.dropAfter(index).apply {
                        Log.d(LOG_TAG, "new stack: $stack")
                        fragmentManager.commit {
                            setReorderingAllowed(true)
                            setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right)
                            replace(
                                container,
                                stack.last().cache.get() ?: provideFragment(
                                    lastDestination!!.destinationKey,
                                    lastDestination!!.args
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
