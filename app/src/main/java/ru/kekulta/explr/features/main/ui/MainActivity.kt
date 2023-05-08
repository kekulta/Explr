package ru.kekulta.explr.features.main.ui

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import by.kirich1409.viewbindingdelegate.viewBinding
import ru.kekulta.explr.R
import ru.kekulta.explr.databinding.ActivityMainBinding
import ru.kekulta.explr.features.main.domain.MainNavigator
import ru.kekulta.explr.features.main.domain.presentation.MainViewModel
import ru.kekulta.explr.shared.utils.checkFilesPermissions


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
            binding.navigationView.menu.findItem(state.drawerItem).isChecked = true
            binding.topAppBar.menu.apply {
                findItem(R.id.hidden_item).isChecked = state.hidden
                findItem(R.id.nomedia_item).isChecked = state.nomedia
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