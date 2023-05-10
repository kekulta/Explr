package ru.kekulta.explr.features.list.ui

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.CreateMethod
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.launch
import ru.kekulta.explr.R
import ru.kekulta.explr.databinding.DetailsItemBinding
import ru.kekulta.explr.databinding.FragmentListBinding
import ru.kekulta.explr.di.MainServiceLocator
import ru.kekulta.explr.features.list.domain.models.enums.SortType
import ru.kekulta.explr.features.list.domain.models.states.FilesListState
import ru.kekulta.explr.features.list.domain.models.events.ListEvent
import ru.kekulta.explr.features.list.domain.presentation.FilesListViewModel
import ru.kekulta.explr.shared.utils.dayOfMonth
import ru.kekulta.explr.shared.utils.getMonth
import ru.kekulta.explr.shared.utils.getParcelableSafe
import ru.kekulta.explr.shared.utils.mime
import ru.kekulta.explr.shared.utils.month
import ru.kekulta.explr.shared.utils.openFile
import ru.kekulta.explr.shared.utils.shareFile
import ru.kekulta.explr.shared.utils.sizeInKb


class FilesListFragment : Fragment() {

    private val binding: FragmentListBinding by viewBinding(createMethod = CreateMethod.INFLATE)
    private val viewModel: FilesListViewModel by viewModels({ requireActivity() }) { FilesListViewModel.Factory }
    private var state: FilesListState? = null
    private val filesAdapter = FilesAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        state = arguments?.getParcelableSafe(STATE_KEY, FilesListState::class.java)
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

            lifecycleScope.launch {
                viewModel.eventsFlow.collect { event ->
                    when (event) {
                        is ListEvent.OpenFile -> {
                            event.file.openFile(requireContext())
                        }

                        is ListEvent.ShareFile -> {
                            event.file.shareFile(requireContext())
                        }

                        is ListEvent.DetailsFile -> {

                            val binding = DetailsItemBinding.inflate(layoutInflater)
                            binding.fileNameDetails.text =
                                getString(R.string.filename_details_placeholder).format(event.file.name)
                            binding.fileSizeDetails.text =
                                getString(R.string.size_details_placeholder).format(event.file.sizeInKb.toInt())
                            binding.fileLastModifiedDetails.text =
                                getString(R.string.last_modified_details_placeholder)
                                    .format(
                                        getMonth(month(event.file.lastModified)),
                                        dayOfMonth(event.file.lastModified)
                                    )
                            binding.fileMimeDetails.text =
                                getString(R.string.mime_type_details_placeholder).format(
                                    event.file.mime(
                                        requireContext()
                                    )
                                )
                            binding.fileTypeDetails.text =
                                getString(R.string.file_type_details_placeholder).format(
                                    getString(event.file.type.text)
                                )
                            MaterialAlertDialogBuilder(requireContext())
                                .setView(binding.root)
                                .setTitle(getString(R.string.details_title))
                                .setNegativeButton(getString(R.string.close)) { _, _ -> }
                                .show()
                        }
                    }
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