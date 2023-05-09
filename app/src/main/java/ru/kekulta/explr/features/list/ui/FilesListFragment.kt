package ru.kekulta.explr.features.list.ui

import android.content.Intent
import android.os.Build
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
import ru.kekulta.explr.features.list.domain.models.FilesListState
import ru.kekulta.explr.features.list.domain.presentation.FilesListViewModel
import ru.kekulta.explr.features.main.domain.models.ToolBarState
import ru.kekulta.explr.shared.navigation.api.Command
import ru.kekulta.explr.shared.utils.openFile
import ru.kekulta.explr.shared.utils.shareFile
import java.io.File


class FilesListFragment : Fragment() {

    private val binding: FragmentListBinding by viewBinding(createMethod = CreateMethod.INFLATE)
    private val viewModel: FilesListViewModel by viewModels({ requireActivity() }) { FilesListViewModel.Factory }
    private var state: FilesListState? = null
    private val filesAdapter = FilesAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        state = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getParcelable(STATE_KEY, FilesListState::class.java)
        } else {
            arguments?.getParcelable(STATE_KEY)
        }
        Log.d(LOG_TAG, "onCreate: ${state?.path}")

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d(LOG_TAG, "onCreateView: ${state?.path}")
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        state?.path?.let {
            lifecycleScope.launch {
                viewModel.subscribe(it).collect { list ->
                    filesAdapter.submitList(list)
                }
            }


            binding.filesRecycler.apply {
                adapter = filesAdapter.apply {
                    onEventListener = viewModel::fileEvent
                }
                layoutManager = LinearLayoutManager(requireContext())
            }

        }

        viewModel.onResume(state)
    }

    companion object {
        const val STATE_KEY = "FilesStateKey"
        const val LOG_TAG = "ListFragment"
        const val DESTINATION_KEY = "ListFragmentKey"
    }
}