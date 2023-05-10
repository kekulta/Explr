package ru.kekulta.explr.features.main.ui

import android.os.Bundle
import android.util.Log
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.launch
import ru.kekulta.explr.R
import ru.kekulta.explr.databinding.ActivityMainBinding
import ru.kekulta.explr.di.MainServiceLocator
import ru.kekulta.explr.features.list.domain.models.enums.SortType
import ru.kekulta.explr.features.main.domain.MainNavigator
import ru.kekulta.explr.features.main.domain.models.MainEvent
import ru.kekulta.explr.features.main.domain.presentation.MainViewModel
import ru.kekulta.explr.shared.utils.checkFilesPermissions
import ru.kekulta.explr.shared.utils.contentEquals
import ru.kekulta.explr.shared.utils.gone
import ru.kekulta.explr.shared.utils.hideScrollBar
import ru.kekulta.explr.shared.utils.scrollToBottom
import ru.kekulta.explr.shared.utils.visible


class MainActivity : AppCompatActivity(R.layout.activity_main) {

    private val binding: ActivityMainBinding by viewBinding(ActivityMainBinding::bind)
    private val viewModel: MainViewModel by viewModels { MainViewModel.Factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        onBackPressedDispatcher.addCallback(
            this,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    viewModel.onBackPressed()
                }
            }
        )

        binding.pathScrollview.hideScrollBar()

        binding.topAppBar.setOnMenuItemClickListener { menuItem ->
            viewModel.settingsClicked(menuItem.itemId)
        }

        binding.topAppBar.setNavigationOnClickListener {
            binding.drawerLayout.open()
        }

        binding.navigationView.setNavigationItemSelectedListener { menuItem ->
            binding.drawerLayout.close()
            viewModel.drawerClicked(menuItem.itemId)
        }

        viewModel.state.observe(this) { state ->
            binding.topAppBar.menu.apply {
                findItem(R.id.hidden_item).isChecked = state.filterState.showHidden
                findItem(R.id.nomedia_item).isChecked = state.filterState.showNomedia
            }
            (listOf(resources.getString(state.toolBarState.root.root)) + state.toolBarState.location.toList()).joinToString(
                separator = " -> "
            ).let { path ->
                state.toolBarState.let { toolBarState ->
                    binding.navigationView.menu.findItem(toolBarState.root.id).isChecked = true

                    binding.topAppBar.menu.apply {
                        findItem(R.id.sorting_item).isVisible = toolBarState.isSortingAvailable
                        findItem(R.id.reverse_item).isVisible = toolBarState.isSortingAvailable
                    }

                    if (toolBarState.location.isEmpty()) {
                        binding.pathTextview.gone()
                        binding.pathTextview.text = ""
                        binding.topAppBar.title = getString(toolBarState.root.root)
                    } else {
                        binding.pathTextview.visible()
                        if (binding.pathTextview.text != path) {
                            binding.pathTextview.text = path
                            binding.pathScrollview.let { scroll ->
                                scroll.post { scroll.scrollToBottom() }
                            }
                            binding.topAppBar.title = toolBarState.location.last()
                        }
                    }
                }


            }
        }
        lifecycleScope.launch {
            viewModel.eventsFlow.collect { event ->
                when (event) {
                    is MainEvent.ShowSortMenu -> {

                        val options =
                            SortType.values().map { getString(it.id) }.toTypedArray()
                        val current = event.state.ordinal

                        MaterialAlertDialogBuilder(this@MainActivity)
                            .setTitle(getString(R.string.sort_by_title))
                            .setSingleChoiceItems(options, current) { _, option ->
                                MainServiceLocator.provideSortingManager().type =
                                    SortType.values()[option]
                            }
                            .setNegativeButton(getString(R.string.close)) { _, _ -> }
                            .show()
                    }
                }
            }
        }


        Log.d(LOG_TAG, "On create")
    }

    override fun onResume() {
        super.onResume()
        viewModel.onResume(MainNavigator(this, supportFragmentManager, R.id.main_container))

        if (viewModel.permissionsRequested) {
            viewModel.permissionsRequested = false
            if (!checkFilesPermissions()) {
                viewModel.permissionsDenied()
            } else {
                viewModel.permissionsGranted()
            }
        } else {
            if (!checkFilesPermissions()) {
                viewModel.noPermissions()
            }
        }
    }

    override fun onPause() {
        super.onPause()
        viewModel.onPause()
    }

    companion object {
        const val LOG_TAG = "MainActivity"
    }
}