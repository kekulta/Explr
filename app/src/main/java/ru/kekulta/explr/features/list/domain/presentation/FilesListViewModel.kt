package ru.kekulta.explr.features.list.domain.presentation

import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import ru.kekulta.explr.di.MainServiceLocator
import ru.kekulta.explr.features.list.domain.api.usecases.FileUtil
import ru.kekulta.explr.features.list.domain.api.usecases.FilesInteractor
import ru.kekulta.explr.features.list.domain.models.enums.Category
import ru.kekulta.explr.features.list.domain.models.events.FileClickEvent
import ru.kekulta.explr.features.list.domain.models.FileRepresentation
import ru.kekulta.explr.features.list.domain.models.states.FilesListState
import ru.kekulta.explr.features.list.domain.models.events.ListEvent
import ru.kekulta.explr.features.list.ui.FilesListFragment
import ru.kekulta.explr.features.main.domain.api.ToolBarManager
import ru.kekulta.explr.features.main.domain.models.ToolBarState
import ru.kekulta.explr.shared.navigation.api.Command
import ru.kekulta.explr.shared.utils.deleteRecursively
import ru.kekulta.explr.shared.utils.file
import ru.kekulta.explr.shared.utils.requireParent
import java.io.File

class FilesListViewModel(
    private val filesInteractor: FilesInteractor,
    private val toolBarManager: ToolBarManager,
    private val fileUtil: FileUtil,
) : ViewModel() {
    private var state: FilesListState? = null
    private val eventChannel = Channel<ListEvent>(Channel.BUFFERED)
    val eventsFlow = eventChannel.receiveAsFlow()

    fun fileEvent(event: FileClickEvent) {
        when (event) {
            is FileClickEvent.Click -> {
                File(event.file.path).let { file ->
                    if (file.exists()) {
                        if (file.isDirectory) {
                            navigateTo(file)
                        } else {
                            viewModelScope.launch { eventChannel.send(ListEvent.OpenFile(file)) }
                        }
                    }
                }
            }

            is FileClickEvent.Delete -> {
                viewModelScope.launch {
                    event.file.deleteRecursively()
                    filesInteractor.update(event.file.requireParent())
                }
            }

            is FileClickEvent.Share -> {
                viewModelScope.launch { eventChannel.send(ListEvent.ShareFile(event.file.file)) }
            }

            is FileClickEvent.Details -> {
                viewModelScope.launch { eventChannel.send(ListEvent.DetailsFile(event.file)) }
            }
        }
    }

    fun subscribe(path: String): Flow<List<FileRepresentation>> =
        filesInteractor.observeContent(path)

    suspend fun update(path: String) {
        filesInteractor.update(path)
    }

    fun onResume(state: FilesListState?) {
        this.state = state
        toolBarManager.toolBarState = ToolBarState(
            state?.root ?: Category.STORAGE, state?.location ?: arrayOf()
        )
    }

    private fun navigateTo(file: File) {
        MainServiceLocator.provideRouter().navigate(
            Command.ForwardTo(
                FilesListFragment.DESTINATION_KEY,
                bundleOf(FilesListFragment.STATE_KEY to state?.let {
                    it.copy(
                        location = it.location + file.name, path = file.path
                    )
                })
            )
        )
    }


    companion object {
        const val LOG_TAG = "FilesListViewModel"
        val Factory = viewModelFactory {
            initializer {
                MainServiceLocator.run {
                    FilesListViewModel(
                        provideFilesInteractor(),
                        provideToolBarManager(),
                        provideFileUtil(),
                    )
                }
            }
        }
    }
}