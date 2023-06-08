package com.smart.cleaner.phoneclean.presentation.ui.duplicates_files

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.SimpleItemAnimator
import by.kirich1409.viewbindingdelegate.viewBinding
import com.smart.cleaner.phoneclean.presentation.R
import com.smart.cleaner.phoneclean.presentation.adapters.DuplicatesFilesParentAdapter
import com.smart.cleaner.phoneclean.presentation.adapters.listeners.OnFileChangeSelectListener
import com.smart.cleaner.phoneclean.presentation.adapters.models.ChildFileItem
import com.smart.cleaner.phoneclean.presentation.adapters.models.ParentFileItem
import com.smart.cleaner.phoneclean.presentation.databinding.FragmentDuplicateFilesBinding
import com.smart.cleaner.phoneclean.presentation.ui.duplicate_images.DuplicateImagesViewModel
import com.smart.cleaner.phoneclean.presentation.ui.models.FilesStateScreen
import com.smart.cleaner.phoneclean.settings.Settings
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class DuplicateFilesFragment : Fragment(R.layout.fragment_duplicate_files) {

    @Inject
    lateinit var settings: Settings

    private val binding: FragmentDuplicateFilesBinding by viewBinding()

    private val fileViewModel: DuplicateFilesViewModel by activityViewModels()

    private val imageViewModel: DuplicateImagesViewModel by activityViewModels()

    private val adapter: DuplicatesFilesParentAdapter =
        DuplicatesFilesParentAdapter(object : OnFileChangeSelectListener {
            override fun selectAll(duplicates: ParentFileItem, isSelected: Boolean) {
                fileViewModel.obtainEvent(
                    FilesStateScreen.FileEvent.SelectAll(
                        duplicates,
                        isSelected
                    )
                )
            }

            override fun selectFile(file: ChildFileItem, isSelected: Boolean) {
                fileViewModel.obtainEvent(FilesStateScreen.FileEvent.SelectFile(file, isSelected))
            }

        })

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapter()
        initObserverScreenState()
        initListeners()
    }

    override fun onResume() {
        super.onResume()
        fileViewModel.obtainEvent(FilesStateScreen.FileEvent.CheckPermission)
    }

    private fun initObserverScreenState() {
        lifecycleScope.launchWhenResumed {
            fileViewModel.screenState.collect { state ->
                adapter.setData(state.duplicates)
                navigate(state.event)
                render(state)
            }
        }
    }

    private fun navigate(event: FilesStateScreen.FileEvent) {
        if (settings.getOpenInformationDialog()) return
        when (event) {
            is FilesStateScreen.FileEvent.OpenPermissionDialog -> findNavController().navigate(R.id.action_to_requestStoragePermDialog)
            is FilesStateScreen.FileEvent.OpenConfirmationDialog -> findNavController().navigate(
                R.id.action_to_deletionRequestDialog
            )
            is FilesStateScreen.FileEvent.OpenDuplicatesImages -> findNavController().navigate(R.id.action_to_duplicateImagesFragment)
            else -> {}
        }
        fileViewModel.obtainEvent(FilesStateScreen.FileEvent.Default)
    }

    private fun render(state: FilesStateScreen) {
        with(binding) {
            btnDelete.isEnabled =
                (state.totalSize + imageViewModel.screenState.value.totalSize) != 0L
            groupStartLoading.isVisible = state.isLoading && state.hasPermission
            groupNotFound.isVisible = state.isNotFound
            groupStopLoading.isVisible = !state.isLoading && state.hasPermission
        }
    }

    private fun initAdapter() {
        binding.recyclerView.adapter = adapter
        val layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.recyclerView.layoutManager = layoutManager
        val animator = binding.recyclerView.itemAnimator
        if (animator is SimpleItemAnimator) {
            animator.supportsChangeAnimations = false
        }
    }

    private fun initListeners() {
        with(binding) {
            btnDelete.setOnClickListener {
                fileViewModel.obtainEvent(FilesStateScreen.FileEvent.OpenConfirmationDialog)
            }
            containerImage.setOnClickListener {
                fileViewModel.obtainEvent(FilesStateScreen.FileEvent.OpenDuplicatesImages)
            }
        }
    }
}