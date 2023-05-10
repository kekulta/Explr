package ru.kekulta.explr.features.difflist.domain.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import ru.kekulta.explr.di.MainServiceLocator
import ru.kekulta.explr.features.difflist.domain.api.usecases.HashedInteractor
import ru.kekulta.explr.features.difflist.domain.models.HashedListState
import ru.kekulta.explr.features.list.domain.models.enums.Category
import ru.kekulta.explr.features.main.domain.api.ToolBarManager
import ru.kekulta.explr.features.main.domain.models.ToolBarState

class HashedListViewModel(
    private val hashedInteractor: HashedInteractor,
    private val toolBarManager: ToolBarManager
) : ViewModel() {
    private val _state = MediatorLiveData(HashedListState())
    private val requireState: HashedListState get() = requireNotNull(_state.value) { "State can not be null!" }
    val state: LiveData<HashedListState> get() = _state

    init {
        _state.addSource(hashedInteractor.observeOld().asLiveData()) { oldFiles ->
            _state.value = requireState.copy(oldFiles = oldFiles)
        }
        _state.addSource(hashedInteractor.observeNew().asLiveData()) { newFiles ->
            _state.value = requireState.copy(newFiles = newFiles)
        }
        _state.addSource(hashedInteractor.observeChanged().asLiveData()) { changedFiles ->
            _state.value = requireState.copy(changedFiles = changedFiles)
        }
        _state.addSource(hashedInteractor.observeDeleted().asLiveData()) { deletedFiles ->
            _state.value = requireState.copy(deletedFiles = deletedFiles)
        }
    }

    fun onViewCreated() {
        toolBarManager.toolBarState = ToolBarState(
            root = Category.DIFFS
        )
    }


    companion object {
        const val LOG_TAG = "HashedListViewModel"
        val Factory = viewModelFactory {
            initializer {
                MainServiceLocator.run {
                    HashedListViewModel(
                        provideHashedInteractor(),
                        provideToolBarManager()
                    )
                }
            }
        }
    }
}