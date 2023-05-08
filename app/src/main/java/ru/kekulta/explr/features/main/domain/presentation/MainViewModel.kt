package ru.kekulta.explr.features.main.domain.presentation

import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import ru.kekulta.explr.R
import ru.kekulta.explr.di.MainServiceLocator
import ru.kekulta.explr.features.list.domain.api.FilesInteractor
import ru.kekulta.explr.features.list.domain.api.VisibilityManager
import ru.kekulta.explr.features.list.ui.FilesListFragment
import ru.kekulta.explr.features.main.ui.PermissionsDeniedFragment
import ru.kekulta.explr.features.main.ui.PermissionsRequestFragment
import ru.kekulta.explr.shared.navigation.api.Command
import ru.kekulta.explr.shared.navigation.api.Navigator
import ru.kekulta.explr.shared.navigation.api.Router


class MainViewModel(private val router: Router, private val visibilityManager: VisibilityManager) :
    ViewModel() {

    private val _drawer = MutableLiveData<Int>(R.id.internal_storage)
    private val _state = MediatorLiveData(MainState(R.id.internal_storage, false, false))
    val state: LiveData<MainState> get() = _state
    var permissionsRequested: Boolean = false
    private var initialized = false

    init {
        _state.addSource(_drawer) { drawerItem ->
            _state.value = _state.value!!.copy(drawerItem = drawerItem)
        }
        _state.addSource(visibilityManager.getHiddenStatus().asLiveData()) { hidden ->
            _state.value = _state.value!!.copy(hidden = hidden)
        }
        _state.addSource(visibilityManager.getNomediaStatus().asLiveData()) { nomedia ->
            _state.value = _state.value!!.copy(nomedia = nomedia)
        }
    }

    fun onResume(navigator: Navigator) {
        router.attachNavigator(navigator)
        if (!initialized) {
            initialized = true


            router.navigateToList(FilesInteractor.STORAGE_CATEGORY, "InternalStorage")
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

    fun drawerClicked(itemId: Int): Boolean {
        when (itemId) {
            R.id.internal_storage -> {
                _drawer.value = itemId
                router.navigateToList(FilesInteractor.STORAGE_CATEGORY, "Internal Storage")
            }

            R.id.audio_item -> {
                _drawer.value = itemId
                router.navigateToList(FilesInteractor.AUDIO_CATEGORY, "Audio")
            }

            R.id.image_item -> {
                _drawer.value = itemId
                router.navigateToList(FilesInteractor.IMAGES_CATEGORY, "Images")
            }

            R.id.documents_item -> {
                _drawer.value = itemId
                router.navigateToList(FilesInteractor.DOCUMENTS_CATEGORY, "Documents")
            }

            R.id.videos_item -> {
                _drawer.value = itemId
                router.navigateToList(FilesInteractor.VIDEOS_CATEGORY, "Video")
            }

            R.id.downloads_item -> {
                _drawer.value = itemId
                router.navigateToList(FilesInteractor.DOWNLOADS_CATEGORY, "Downloads")
            }

            R.id.recent_item -> {
                _drawer.value = itemId
                router.navigateToList(FilesInteractor.RECENT_CATEGORY, "Recent")
            }
        }
        return true
    }

    //TODO change extension
    private fun Router.navigateToList(path: String, location: String) {
        this.navigate(
            Command.ForwardTo(
                FilesListFragment.DESTINATION_KEY,
                Bundle().apply {
                    putString(
                        FilesListFragment.PATH_KEY,
                        path
                    )
                    putStringArray(FilesListFragment.LOCATION_KEY, arrayOf(location))
                }
            )
        )
    }

    fun settingsClicked(itemId: Int): Boolean =
        when (itemId) {
            R.id.hidden_item -> {
                visibilityManager.showHidden = !visibilityManager.showHidden
                true
            }

            R.id.nomedia_item -> {
                visibilityManager.showNomedia = !visibilityManager.showNomedia
                true
            }

            else -> false
        }

    companion object {
        const val LOG_TAG = "MainViewModel"
        val Factory = viewModelFactory {
            initializer {
                MainViewModel(
                    MainServiceLocator.provideRouter(),
                    MainServiceLocator.provideVisibilityManager()
                )
            }
        }
    }
}