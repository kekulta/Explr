package ru.kekulta.explr.features.difflist.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import ru.kekulta.explr.features.difflist.domain.models.enums.ChangeType

class HashedListPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 4

    override fun createFragment(position: Int): Fragment {
        val fragment = HashedListPageFragment()
        fragment.arguments = Bundle().apply {
            putSerializable(HashedListPageFragment.TYPE_KEY, ChangeType.values()[position + 1])
        }
        return fragment
    }
}
