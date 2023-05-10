package ru.kekulta.explr.features.main.ui


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.CreateMethod
import by.kirich1409.viewbindingdelegate.viewBinding
import ru.kekulta.explr.R
import ru.kekulta.explr.databinding.FragmentPermissionsRequestBinding
import ru.kekulta.explr.features.main.domain.presentation.MainViewModel
import ru.kekulta.explr.shared.utils.requestFilesPermissions


class PermissionsRequestFragment : Fragment(R.layout.fragment_permissions_request)   {
    private val mainViewModel: MainViewModel by viewModels({ requireActivity() }) { MainViewModel.Factory }

    private val binding: FragmentPermissionsRequestBinding by viewBinding(createMethod = CreateMethod.INFLATE)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d(LOG_TAG, "View created")

        binding.permissionsButton.setOnClickListener {
            mainViewModel.permissionsRequested = true
            requireActivity().requestFilesPermissions()
        }
    }

    companion object {
        const val LOG_TAG = "PermissionsRequestFragment"
        const val DESTINATION_KEY = "PermissionsRequestFragmentKey"
    }
}