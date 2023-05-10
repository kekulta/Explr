package ru.kekulta.explr.features.difflist.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.CreateMethod
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.tabs.TabLayoutMediator
import ru.kekulta.explr.databinding.FragmentHashedListBinding
import ru.kekulta.explr.features.difflist.domain.models.enums.ChangeType
import ru.kekulta.explr.features.difflist.domain.presentation.HashedListViewModel

class HashedListFragment : Fragment() {
    private val binding: FragmentHashedListBinding by viewBinding(createMethod = CreateMethod.INFLATE)
    private val viewModel: HashedListViewModel by viewModels({ requireActivity() }) { HashedListViewModel.Factory }
    private val hashedAdapter by lazy { HashedListPagerAdapter(this) }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.onViewCreated()

        binding.diffPager.apply {
            adapter = hashedAdapter
        }

        TabLayoutMediator(binding.diffTabLayout, binding.diffPager) { tab, position ->
            tab.text = getString(ChangeType.values()[position + 1].text)
        }.attach()

    }

    companion object {
        const val STATE_KEY = "HashedStateKey"
        const val DESTINATION_KEY = "HashedFragmentKey"
        const val LOG_TAG = "HashedListFragment"
    }
}