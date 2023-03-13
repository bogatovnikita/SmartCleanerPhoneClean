package yin_kio.permissions_views

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.permissions.requestStoragePermissions
import com.example.permissions.runtimePermissionsLauncher
import yin_kio.permissions_views.databinding.FragmentPermissionBinding

abstract class PermissionFragment<VM> : Fragment(R.layout.fragment_permission) {

    private val binding: FragmentPermissionBinding by viewBinding()
    private val viewModel: VM by lazy { provideViewModel() }


    private val permissionsLauncher by runtimePermissionsLauncher {}

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {


        binding.givePermission.setOnClickListener {
            requestStoragePermissions(permissionsLauncher)
        }
    }

    override fun onResume() {
        super.onResume()
        actionOnResume(viewModel)
    }
    protected abstract fun provideViewModel() : VM
    protected abstract fun actionOnResume(viewModel: VM)

}