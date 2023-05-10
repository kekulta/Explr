package ru.kekulta.explr.features.difflist.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.CreateMethod
import by.kirich1409.viewbindingdelegate.viewBinding
import kotlinx.coroutines.launch
import ru.kekulta.explr.R
import ru.kekulta.explr.databinding.FragmentHashedListBinding
import ru.kekulta.explr.databinding.FragmentHashedListPageBinding
import ru.kekulta.explr.features.difflist.domain.models.enums.ChangeType
import ru.kekulta.explr.features.difflist.domain.presentation.HashedListViewModel
import ru.kekulta.explr.shared.utils.getParcelableSafe
import ru.kekulta.explr.shared.utils.getSerializableSafe

class HashedListPageFragment : Fragment() {
    private val binding: FragmentHashedListPageBinding by viewBinding(createMethod = CreateMethod.INFLATE)
    private val viewModel: HashedListViewModel by viewModels({ requireActivity() }) { HashedListViewModel.Factory }
    private var type: ChangeType? = null
    private val recyclerAdapter = HashedListRecyclerAdapter()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        type = arguments?.getSerializableSafe(TYPE_KEY, ChangeType::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.changesRecycler.apply {
            adapter = recyclerAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
        lifecycleScope.launch {
            viewModel.state.observe(viewLifecycleOwner) { state ->
                when (type) {
                    ChangeType.OLD -> state.oldFiles
                    ChangeType.NEW -> state.newFiles
                    ChangeType.CHANGED -> state.changedFiles
                    ChangeType.DELETED -> state.deletedFiles
                    else -> null
                }?.let { list -> recyclerAdapter.submitList(list) }
            }
        }
    }

    companion object {
        const val LOG_TAG = "HashedListPageFragment"
        const val TYPE_KEY = "HashedListPageFragmentTypeKey"
    }
}