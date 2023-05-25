package yin_kio.garbage_clean.presentation.garbage_list

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.ColorStateList
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
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
import yin_kio.garbage_clean.data.CleanTimeImpl
import yin_kio.garbage_clean.data.FilesImpl
import yin_kio.garbage_clean.data.PermissionsImpl
import yin_kio.garbage_clean.data.StorageInfoImpl
import yin_kio.garbage_clean.domain.GarbageFilesFactory
import yin_kio.garbage_clean.domain.services.garbage_files.GarbageType
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
            viewModel.scanOrClean()
        }
    }

    private val adapter = GarbageAdapter(
        onItemUpdate = {garbageType, file, checkable -> viewModel.updateItemSelection(garbageType, file, checkable)},
        onGroupUpdate = {garbageType, checkable,-> viewModel.updateGroupSelection(garbageType, checkable)},
        onGroupClick = {garbageType,checkable -> viewModel.switchGroupSelection(garbageType, checkable)},
        onItemClick = {garbageType,file,checkable -> viewModel.switchItemSelection(garbageType, file, checkable)}
    )

    override fun onStart() {
        super.onStart()
        viewModel.updateLanguage()
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        binding.recycler.adapter = adapter

        binding.button.setOnClickListener { viewModel.scanOrClean() }

        setupStateObserver()
        setupCommandsObserver()

    }


    @SuppressLint("NotifyDataSetChanged")
    private fun setupStateObserver() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.state.collect {
                binding.apply {
                    size.text = it.sizeText
                    button.text = it.buttonText
                    permissionRequired.isInvisible = !it.isShowPermissionRequired
                    button.alpha = it.buttonOpacity
                    message.text = it.message

                    message.setTextColor(it.messageColor)
                    size.setTextColor(it.sizeMessageColor)
                    sizeIcon.imageTintList = ColorStateList.valueOf(it.sizeMessageColor)

                    adapter.onExpandListenerEnabled = it.isExpandEnabled
                    infoPlate.isInvisible = !it.isInfoVisible
                }

                adapter.garbage = it.garbageGroups
                adapter.notifyDataSetChanged()

            }
        }
    }

    private fun setupCommandsObserver() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.commands.collect { command ->
                when (command) {
                    Command.ShowPermissionDialog -> findNavController().navigate(R.id.toPermissionDialog)
                    Command.ClosePermissionDialog -> findNavController().navigateUp()
                    Command.RequestPermission -> {
                        requestStoragePermissions()
                        findNavController().navigateUp()
                    }
                    is Command.UpdateGroup -> {
                        adapter.notifyGroupChange(groupPosition(command.garbageType))
                    }
                    is Command.UpdateChildrenAndGroup -> updateChildrenAndGroup(command)
                    Command.ShowCleanProgress -> findNavController().navigate(R.id.toCleanProgress)
                    else -> {}
                }
            }
        }
    }

    private fun groupPosition(group: GarbageType) =
        adapter.garbage.indexOfFirst { it.type == group }

    private fun updateChildrenAndGroup(command: Command.UpdateChildrenAndGroup) {

        val groupPosition = groupPosition(command.garbageType)
        adapter.notifyGroupChange(groupPosition)

        for (childPosition in adapter.garbage[groupPosition].files.indices) {
            adapter.notifyChildChange(groupPosition, childPosition)
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


    private fun LifecycleAware.createViewModel(): ViewModel {
        val context = requireContext().applicationContext

        val presenter = Presenter(context)
        val uiOuter = UIOuterImpl(
            presenter = presenter,
            context = context
        )

        val useCases = GarbageFilesFactory.createGarbageFilesUseCases(
            uiOuter = uiOuter,
            permissions = PermissionsImpl(context),
            files = FilesImpl(),
            storageInfo = StorageInfoImpl(context),
            coroutineScope = viewModelScope,
            cleanTime = CleanTimeImpl(context)
        )

        val viewModel = ViewModel(
            useCases = useCases,
            coroutineScope = viewModelScope
        )

        uiOuter.viewModel = viewModel



        return viewModel
    }
}