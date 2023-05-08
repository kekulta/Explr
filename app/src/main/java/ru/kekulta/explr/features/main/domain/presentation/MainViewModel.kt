package ru.kekulta.explr.features.main.domain.presentation

import android.os.Bundle
import android.os.Environment
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import ru.kekulta.explr.di.MainServiceLocator
import ru.kekulta.explr.features.list.domain.api.FilesRepository
import ru.kekulta.explr.features.list.domain.models.FileRepresentation
import ru.kekulta.explr.features.list.domain.models.FileRepresentation.Companion.ROOT
import ru.kekulta.explr.features.list.ui.FilesListFragment
import ru.kekulta.explr.features.main.ui.PermissionsDeniedFragment
import ru.kekulta.explr.features.main.ui.PermissionsRequestFragment
import ru.kekulta.explr.shared.navigation.api.Command
import ru.kekulta.explr.shared.navigation.api.Navigator
import ru.kekulta.explr.shared.navigation.api.Router
import java.io.File


class MainViewModel(private val router: Router) :
    ViewModel() {

    var permissionsRequested: Boolean = false
    private var initialized = false

    fun onResume(navigator: Navigator) {
        router.attachNavigator(navigator)
        if (!initialized) {
            initialized = true


            router.navigate(
                Command.ForwardTo(
                    FilesListFragment.KEY,
                    Bundle().apply {
                        putString(
                            FilesListFragment.LOCATION_KEY,
                            Environment.getExternalStorageDirectory().path
                        )
                    }
                )
            )
        }
    }

    fun onBackPressed() {
        router.navigate(Command.Back)
    }

    fun onPause() {
        router.detachNavigator()
    }

    // TODO fix permission navigation

    fun permissionsDenied() {
        router.navigate(
            Command.ReplaceTo(
                destination = PermissionsDeniedFragment.KEY
            )
        )
    }

    fun permissionsGranted() {
        router.navigate(
            Command.Back
        )
    }

    fun noPermissions() {
        router.navigate(Command.ForwardTo(PermissionsRequestFragment.KEY))
    }

    companion object {
        const val LOG_TAG = "MainViewModel"
        val Factory = viewModelFactory {
            initializer {
                MainViewModel(
                    MainServiceLocator.provideRouter()
                )
            }
        }
    }
}