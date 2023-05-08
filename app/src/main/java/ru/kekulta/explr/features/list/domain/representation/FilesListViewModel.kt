package ru.kekulta.explr.features.list.domain.representation

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.kekulta.explr.di.MainServiceLocator
import ru.kekulta.explr.features.list.domain.api.FilesInteractor
import ru.kekulta.explr.features.list.domain.api.FilesInteractorResponse
import ru.kekulta.explr.features.list.domain.api.FilesRepository
import ru.kekulta.explr.features.list.domain.impl.FilesInteractorImpl
import ru.kekulta.explr.features.list.domain.models.FileRepresentation
import ru.kekulta.explr.features.main.domain.presentation.MainViewModel
import ru.kekulta.explr.shared.navigation.api.Command
import java.io.File

class FilesListViewModel(private val interactor: FilesInteractor) : ViewModel() {


    fun subscribe(path: String): Flow<List<FileRepresentation>> =
        interactor.observeContent(path).also {
            viewModelScope.launch {
                (interactor as FilesInteractorImpl).update(path)
            }
        }


    companion object {
        const val LOG_TAG = "FilesListViewModel"
        val Factory = viewModelFactory {
            initializer {
                FilesListViewModel(MainServiceLocator.provideFilesInteractor())
            }
        }
    }
}