package ru.kekulta.explr.features.main.domain.presentation

import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import ru.kekulta.explr.R
import ru.kekulta.explr.di.MainServiceLocator
import ru.kekulta.explr.features.difflist.domain.api.usecases.HashedInteractor
import ru.kekulta.explr.features.difflist.ui.HashedListFragment
import ru.kekulta.explr.features.list.domain.api.usecases.FilterManager
import ru.kekulta.explr.features.list.domain.api.usecases.SortingManager
import ru.kekulta.explr.features.list.domain.models.enums.Category
import ru.kekulta.explr.features.list.domain.models.states.FilesListState
import ru.kekulta.explr.features.list.domain.models.states.FilterState
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
import ru.kekulta.explr.shared.navigation.models.Backstack


class MainViewModel(
    private val router: Router,
    private val filterManager: FilterManager,
    private val sortingManager: SortingManager,
    private val toolBarManager: ToolBarManager,
    private val hashedInteractor: HashedInteractor,
) :
    ViewModel() {


    private val _state =
        MediatorLiveData(
            MainState(
                filterState = FilterState(),
                toolBarState = ToolBarState()
            )
        )
    private var backstack = Backstack()
    private val eventChannel = Channel<MainEvent>(Channel.BUFFERED)
    val eventsFlow = eventChannel.receiveAsFlow()
    val state: LiveData<MainState> get() = _state
    var permissionsRequested: Boolean = false
    private var initialized = false

    init {
        _state.addSource(filterManager.getFilterStateFlow().asLiveData()) { filterState ->
            _state.value = _state.value!!.copy(filterState = filterState)
        }
        _state.addSource(toolBarManager.getCurrentToolBarStateFlow().asLiveData()) { toolBarState ->
            _state.value = _state.value!!.copy(toolBarState = toolBarState)
        }
    }

    fun onResume(navigator: Navigator) {
        router.attachNavigator(navigator, backstack)
        if (!initialized) {
            initialized = true
            viewModelScope.launch {
                hashedInteractor.updateStart()
            }
            router.navigateToList(Category.STORAGE)
        }
    }

    fun onBackPressed() {
        router.navigate(Command.Back)
    }

    fun onPause() {
        backstack = router.detachNavigator()
    }

    // TODO fix permission navigation

    fun permissionsDenied() {
        router.navigate(
            Command.ReplaceTo(
                destinationKey = PermissionsDeniedFragment.DESTINATION_KEY
            )
        )
    }

    fun permissionsGranted() {
        viewModelScope.launch {
            hashedInteractor.updateStart()
        }
        router.navigate(
            Command.Back
        )
    }

    fun noPermissions() {
        router.navigate(Command.ForwardTo(PermissionsRequestFragment.DESTINATION_KEY))
    }

    fun drawerClicked(itemId: Int): Boolean {
        when (itemId) {
            R.id.internal_storage -> {
                router.navigateToList(Category.STORAGE)
            }

            R.id.diffs_storage -> {
                router.navigateToDiff()
            }

            R.id.audio_item -> {
                router.navigateToList(Category.AUDIO)
            }

            R.id.image_item -> {
                router.navigateToList(Category.IMAGES)
            }

            R.id.documents_item -> {
                router.navigateToList(Category.DOCUMENTS)
            }

            R.id.videos_item -> {
                router.navigateToList(Category.VIDEOS)
            }

            R.id.downloads_item -> {
                router.navigateToList(Category.DOWNLOADS)
            }

            R.id.recent_item -> {
                router.navigateToList(Category.RECENT)
            }
        }
        return true
    }

    private fun Router.navigateToDiff() {
        this.navigate(
            Command.ForwardTo(
                HashedListFragment.DESTINATION_KEY,
            )
        )
    }

    private fun Router.navigateToList(category: Category) {
        this.navigate(
            Command.ForwardTo(
                FilesListFragment.DESTINATION_KEY,
                Bundle().apply {
                    putParcelable(
                        FilesListFragment.STATE_KEY,
                        FilesListState(category.path, category)
                    )
                }
            )
        )
    }

    fun settingsClicked(itemId: Int): Boolean =
        when (itemId) {
            R.id.hidden_item -> {
                filterManager.filterState =
                    filterManager.filterState.copy(showHidden = !filterManager.filterState.showHidden)
                true
            }

            R.id.nomedia_item -> {
                filterManager.filterState =
                    filterManager.filterState.copy(showNomedia = !filterManager.filterState.showNomedia)
                true
            }

            R.id.sorting_item -> {
                viewModelScope.launch { eventChannel.send(MainEvent.ShowSortMenu(sortingManager.type)) }
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
                MainServiceLocator.run {
                    MainViewModel(
                        provideRouter(),
                        provideFilterManager(),
                        provideSortingManager(),
                        provideToolBarManager(),
                        provideHashedInteractor(),
                    )
                }
            }
        }
    }
}