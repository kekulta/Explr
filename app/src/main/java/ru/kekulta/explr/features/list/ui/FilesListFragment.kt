package ru.kekulta.explr.features.list.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.FileProvider
import androidx.core.content.FileProvider.getUriForFile
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.CreateMethod
import by.kirich1409.viewbindingdelegate.viewBinding
import kotlinx.coroutines.launch
import ru.kekulta.explr.App
import ru.kekulta.explr.R
import ru.kekulta.explr.databinding.FragmentListBinding
import ru.kekulta.explr.di.MainServiceLocator
import ru.kekulta.explr.features.list.domain.models.FileClickEvent
import ru.kekulta.explr.features.list.domain.presentation.FilesListViewModel
import ru.kekulta.explr.features.main.domain.models.ToolBarState
import ru.kekulta.explr.shared.navigation.api.Command
import ru.kekulta.explr.shared.utils.openFile
import ru.kekulta.explr.shared.utils.shareFile
import java.io.File


class FilesListFragment : Fragment() {

    private val binding: FragmentListBinding by viewBinding(createMethod = CreateMethod.INFLATE)
    private val viewModel: FilesListViewModel by viewModels({ requireActivity() }) { FilesListViewModel.Factory }
    private var path: String? = null
    private var root: Int? = null
    private var location: Array<String>? = null
    private val filesAdapter = FilesAdapter().apply {
        onEventListener = { event ->
            when (event) {
                is FileClickEvent.Click -> {
                    File(event.file.path).let { file ->
                        if (file.exists()) {
                            if (file.isDirectory) {
                                MainServiceLocator.provideRouter().navigate(
                                    //TODO Fix navigation
                                    Command.ForwardTo(
                                        DESTINATION_KEY, bundleOf(
                                            PATH_KEY to file.path,
                                            ROOT_KEY to root,
                                            LOCATION_KEY to ((location ?: arrayOf()) + file.name),
                                        )
                                    )
                                )
                            } else {
                                file.openFile(requireContext())
                            }
                        }
                    }
                }

                is FileClickEvent.Delete -> {
                    lifecycleScope.launch {
                        File(event.file.path).deleteRecursively()

                    }
                }

                is FileClickEvent.Share -> {
                    event.file.shareFile(requireContext())
                }
            }


        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        path = arguments?.getString(PATH_KEY)
        location = arguments?.getStringArray(LOCATION_KEY)
        root = arguments?.getInt(ROOT_KEY)
        Log.d(LOG_TAG, "onCreate: $path")

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d(LOG_TAG, "onCreateView: $path")
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        path?.let {
            lifecycleScope.launch {
                viewModel.subscribe(it).collect { list ->
                    filesAdapter.submitList(list)
                }
            }


            binding.filesRecycler.apply {

                adapter = filesAdapter
                layoutManager = LinearLayoutManager(requireContext())
            }
        }

        viewModel.onResume(ToolBarState(root ?: R.string.no_root, location ?: arrayOf()))
    }

    companion object {
        const val INTERNAL_STORAGE = "Internal Storage"
        const val ROOT_KEY = "FilesRootKey"
        const val PATH_KEY = "FilesPathKey"
        const val LOCATION_KEY = "FilesLocationKey"
        const val LOG_TAG = "ListFragment"
        const val DESTINATION_KEY = "ListFragmentKey"
    }
}