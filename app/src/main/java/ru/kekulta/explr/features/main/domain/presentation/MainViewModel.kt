package ru.kekulta.explr.features.main.domain.presentation

import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import ru.kekulta.explr.R
import ru.kekulta.explr.di.MainServiceLocator
import ru.kekulta.explr.features.list.domain.api.FilesInteractor
import ru.kekulta.explr.features.list.domain.api.SortingManager
import ru.kekulta.explr.features.list.domain.api.VisibilityManager
import ru.kekulta.explr.features.list.domain.models.Category
import ru.kekulta.explr.features.list.ui.FilesListFragment
import ru.kekulta.explr.features.main.domain.api.ToolBarManager
import ru.kekulta.explr.features.main.domain.models.MainEvent
import ru.kekulta.explr.features.main.domain.models.MainState
import ru.kekulta.explr.features.main.domain.models.ToolBarState
import ru.kekulta.explr.features.main.ui.PermissionsDeniedFragment
import ru.kekulta.explr.features.main.ui.PermissionsRequestFragment
import ru.kekulta.explr.shared.navigation.api.Command
import ru.kekulta.explr.shared.navigation.api.Navigator
import ru.kekulta.explr.shared.navigation.api.Router


class MainViewModel(
    private val router: Router,
    private val visibilityManager: VisibilityManager,
    private val sortingManager: SortingManager,
    toolBarManager: ToolBarManager,
) :
    ViewModel() {

    private val _drawer = MutableLiveData<Int>(R.id.internal_storage)
    private val _state =
        MediatorLiveData(MainState(R.id.internal_storage, false, false, ToolBarState()))
    private val eventChannel = Channel<MainEvent>(Channel.BUFFERED)
    val eventsFlow = eventChannel.receiveAsFlow()
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
        _state.addSource(toolBarManager.getCurrentToolBarStateFlow().asLiveData()) { toolBarState ->
            _state.value = _state.value!!.copy(toolBarState = toolBarState)
        }
    }

    fun onResume(navigator: Navigator) {
        router.attachNavigator(navigator)
        if (!initialized) {
            initialized = true

            router.navigateToList(Category.STORAGE)
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
                router.navigateToList(Category.STORAGE)
            }

            R.id.audio_item -> {
                _drawer.value = itemId
                router.navigateToList(Category.AUDIO)
            }

            R.id.image_item -> {
                _drawer.value = itemId
                router.navigateToList(Category.IMAGES)
            }

            R.id.documents_item -> {
                _drawer.value = itemId
                router.navigateToList(Category.DOCUMENTS)
            }

            R.id.videos_item -> {
                _drawer.value = itemId
                router.navigateToList(Category.VIDEOS)
            }

            R.id.downloads_item -> {
                _drawer.value = itemId
                router.navigateToList(Category.DOWNLOADS)
            }

            R.id.recent_item -> {
                _drawer.value = itemId
                router.navigateToList(Category.DOCUMENTS)
            }
        }
        return true
    }

    //TODO change extension
    private fun Router.navigateToList(category: Category) {
        this.navigate(
            Command.ForwardTo(
                FilesListFragment.DESTINATION_KEY,
                Bundle().apply {
                    putString(
                        FilesListFragment.PATH_KEY,
                        category.path
                    )
                    putInt(FilesListFragment.ROOT_KEY, category.text)
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

            R.id.sorting_item -> {
                viewModelScope.launch { eventChannel.send(MainEvent.SHOW_SORT_MENU) }
                true
            }

            R.id.reverse_item -> {
                sortingManager.reversed = !sortingManager.reversed
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
                    MainServiceLocator.provideVisibilityManager(),
                    MainServiceLocator.provideSortingManager(),
                    MainServiceLocator.provideToolBarManager(),
                )
            }
        }
    }
}