package ru.kekulta.explr.features.list.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.CreateMethod
import by.kirich1409.viewbindingdelegate.viewBinding
import kotlinx.coroutines.launch
import ru.kekulta.explr.databinding.FragmentListBinding
import ru.kekulta.explr.di.MainServiceLocator
import ru.kekulta.explr.features.list.domain.presentation.FilesListViewModel
import ru.kekulta.explr.shared.navigation.api.Command
import java.io.File

class FilesListFragment : Fragment() {

    private val binding: FragmentListBinding by viewBinding(createMethod = CreateMethod.INFLATE)
    private val viewModel: FilesListViewModel by viewModels({ requireActivity() }) { FilesListViewModel.Factory }
    private var path: String? = null
    private var root: Int? = null
    private var location: Array<String>? = null
    private val filesAdapter = FilesAdapter().apply {
        onClickListener = { path ->
            MainServiceLocator.provideRouter().navigate(
                //TODO Fix navigation
                Command.ForwardTo(
                    DESTINATION_KEY, bundleOf(
                        PATH_KEY to path,
                        ROOT_KEY to root,
                        LOCATION_KEY to ((location ?: arrayOf()) + File(path).name),
                    )
                )
            )
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
//        binding.locationTextview.text =
//            location?.joinToString(separator = " -> ") ?: "No such file or directory"
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