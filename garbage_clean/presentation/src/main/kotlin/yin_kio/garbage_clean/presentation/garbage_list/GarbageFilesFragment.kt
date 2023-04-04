package yin_kio.garbage_clean.presentation.garbage_list

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isInvisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.permissions.requestManageExternalStorage
import jamycake.lifecycle_aware.LifecycleAware
import jamycake.lifecycle_aware.currentBackStackEntry
import yin_kio.garbage_clean.data.FilesImpl
import yin_kio.garbage_clean.data.PermissionsImpl
import yin_kio.garbage_clean.data.StorageInfoImpl
import yin_kio.garbage_clean.domain.GarbageFilesFactory
import yin_kio.garbage_clean.presentation.R
import yin_kio.garbage_clean.presentation.databinding.FragmentGarbageFilesBinding
import yin_kio.garbage_clean.presentation.garbage_list.adapter.GarbageAdapter


class GarbageFilesFragment : Fragment(R.layout.fragment_garbage_files) {


    private val binding: FragmentGarbageFilesBinding by viewBinding()
    private val viewModel: ViewModel by currentBackStackEntry { createViewModel() }

    private val permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ){
        if (it[Manifest.permission.READ_EXTERNAL_STORAGE]!!
            && it[Manifest.permission.WRITE_EXTERNAL_STORAGE]!!){
            viewModel.scan()
        }
    }

    private val adapter = GarbageAdapter(
        onItemUpdate = {_,_,_ ->},
        onGroupUpdate = {_,_ ->}
    )


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        binding.recycler.adapter = adapter

        binding.button.setOnClickListener { viewModel.scan() }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.state.collect{
                binding.size.text = it.size
                binding.button.text = it.buttonText

                adapter.garbage = it.garbage
                binding.permissionRequired.isInvisible = !it.isShowPermissionRequired


                adapter.notifyDataSetChanged()

            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.commands.collect{
                when(it){
                    Command.ShowPermissionDialog -> {
                        findNavController().navigate(R.id.toPermissionDialog)
                    }
                    Command.ClosePermissionDialog -> {
                        findNavController().navigateUp()
                    }
                    Command.RequestPermission -> {
                        requestStoragePermissions()
                        findNavController().navigateUp()
                    }
                }
            }
        }

    }

    private fun requestStoragePermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            requireActivity().requestManageExternalStorage()
        } else {
            requestOldStoragePermissions()
        }
    }

    private fun requestOldStoragePermissions() {
        if (shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)
            || shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        ) {
            openAppSettings()
        } else {
            permissionLauncher.launch(
                arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
            )
        }
    }

    private fun openAppSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri: Uri = Uri.fromParts("package", requireContext().packageName, null)
        intent.setData(uri)
        startActivity(intent)
    }


    override fun onStart() {
        super.onStart()
        viewModel.start()
    }



    private fun LifecycleAware.createViewModel(): ViewModel {
        val context = requireContext().applicationContext

        val presenter = Presenter(context)
        val uiOuter = UIOuterImpl(
            presenter = presenter
        )

        val useCases = GarbageFilesFactory.createGarbageFilesUseCases(
            uiOuter = uiOuter,
            permissions = PermissionsImpl(context),
            files = FilesImpl(),
            storageInfo = StorageInfoImpl(context),
            coroutineScope = viewModelScope
        )

        val viewModel = ViewModel(
            useCases = useCases,
            coroutineScope = viewModelScope,
            presenter = presenter
        )

        uiOuter.viewModel = viewModel

        return viewModel
    }
}