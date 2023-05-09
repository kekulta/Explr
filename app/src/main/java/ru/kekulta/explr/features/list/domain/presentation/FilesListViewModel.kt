package ru.kekulta.explr.features.list.domain.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import ru.kekulta.explr.di.MainServiceLocator
import ru.kekulta.explr.features.list.domain.api.FilesInteractor
import ru.kekulta.explr.features.list.domain.impl.FilesInteractorImpl
import ru.kekulta.explr.features.list.domain.models.FileRepresentation
import ru.kekulta.explr.features.main.domain.api.ToolBarManager
import ru.kekulta.explr.features.main.domain.models.ToolBarState

class FilesListViewModel(
    private val interactor: FilesInteractor,
    private val toolBarManager: ToolBarManager
) :
    ViewModel() {


    fun subscribe(path: String): Flow<List<FileRepresentation>> =
        interactor.observeContent(path).also {
            viewModelScope.launch {
                (interactor as FilesInteractorImpl).update(path)
            }
        }

    fun onResume(toolBarState: ToolBarState) {
        toolBarManager.toolBarState = toolBarState
    }


    companion object {
        const val LOG_TAG = "FilesListViewModel"
        val Factory = viewModelFactory {
            initializer {
                FilesListViewModel(
                    MainServiceLocator.provideFilesInteractor(),
                    MainServiceLocator.provideToolBarManager(),
                )
            }
        }
    }
}