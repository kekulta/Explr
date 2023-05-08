package ru.kekulta.explr.features.list.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.CreateMethod
import by.kirich1409.viewbindingdelegate.viewBinding
import kotlinx.coroutines.launch
import ru.kekulta.explr.databinding.FragmentListBinding
import ru.kekulta.explr.di.MainServiceLocator
import ru.kekulta.explr.features.list.domain.representation.FilesListViewModel
import ru.kekulta.explr.shared.navigation.api.Command
import ru.kekulta.explr.shared.utils.shortToast

class FilesListFragment : Fragment() {

    private val binding: FragmentListBinding by viewBinding(createMethod = CreateMethod.INFLATE)
    private val viewModel: FilesListViewModel by viewModels({ requireActivity() }) { FilesListViewModel.Factory }
    private var location: String? = null
    private val filesAdapter = FilesAdapter().apply {
        onClickListener = { path ->
            requireContext().shortToast("going to $path")
            MainServiceLocator.provideRouter().navigate(
                //TODO Fix navigation
                Command.ForwardTo(
                    KEY, bundleOf(
                        LOCATION_KEY to path
                    )
                )
            )
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        location = arguments?.getString(LOCATION_KEY)
        Log.d(LOG_TAG, "onCreate: $location")

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d(LOG_TAG, "onCreateView: $location")
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.locationTextview.text = location ?: "No such file or directory"
        location?.let {
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
        const val LOCATION_KEY = "FilesLocationKey"
        const val LOG_TAG = "ListFragment"
        const val KEY = "ListFragmentKey"
    }
}