package com.smart.cleaner.phoneclean.presentation.ui.duplicate_images

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.smart.cleaner.phoneclean.presentation.R
import com.smart.cleaner.phoneclean.presentation.adapters.DuplicatesImagesParentAdapter
import com.smart.cleaner.phoneclean.presentation.adapters.listeners.OnImageChangeSelectListener
import com.smart.cleaner.phoneclean.presentation.adapters.models.ChildImageItem
import com.smart.cleaner.phoneclean.presentation.adapters.models.ParentImageItem
import com.smart.cleaner.phoneclean.presentation.databinding.FragmentDuplicateImagesBinding
import com.smart.cleaner.phoneclean.presentation.ui.duplicates_files.DuplicateFilesViewModel
import com.smart.cleaner.phoneclean.presentation.ui.models.ImagesStateScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DuplicateImagesFragment : Fragment(R.layout.fragment_duplicate_images) {

    private val imageViewModel: DuplicateImagesViewModel by activityViewModels()

    private val fileViewModel: DuplicateFilesViewModel by activityViewModels()

    private val binding: FragmentDuplicateImagesBinding by viewBinding()

    private val adapter: DuplicatesImagesParentAdapter =
        DuplicatesImagesParentAdapter(object : OnImageChangeSelectListener {
            override fun selectAll(duplicates: ParentImageItem, isSelected: Boolean) {
                imageViewModel.obtainEvent(
                    ImagesStateScreen.ImageEvent.SelectAll(
                        duplicates,
                        isSelected
                    )
                )
            }

            override fun selectImage(image: ChildImageItem, isSelected: Boolean) {
                imageViewModel.obtainEvent(ImagesStateScreen.ImageEvent.SelectImage(image, isSelected))
            }

        })

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapter()
        initListeners()
        initObserverScreenState()
    }

    override fun onResume() {
        super.onResume()
        imageViewModel.obtainEvent(ImagesStateScreen.ImageEvent.CheckPermission)
    }

    private fun initObserverScreenState() {
        lifecycleScope.launchWhenResumed {
            imageViewModel.screenState.collect { state ->
                adapter.submitList(state.duplicates)
                navigate(state.event)
                render(state)
            }
        }
    }

    private fun render(state: ImagesStateScreen) {
        with(binding) {
            btnDelete.isEnabled = (state.totalSize + fileViewModel.screenState.value.totalSize) != 0L
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
    }

    private fun navigate(event: ImagesStateScreen.ImageEvent) {
        when (event) {
            is ImagesStateScreen.ImageEvent.OpenPermissionDialog -> findNavController().navigate(R.id.action_to_requestStoragePermDialog)
            is ImagesStateScreen.ImageEvent.OpenConfirmationDialog -> findNavController().navigate(R.id.action_to_deletionRequestDialog)
            is ImagesStateScreen.ImageEvent.OpenDuplicatesFile -> findNavController().navigate(R.id.action_to_duplicateFilesFragment)
            else -> {}
        }
        imageViewModel.obtainEvent(ImagesStateScreen.ImageEvent.Default)
    }

    private fun initListeners() {
        with(binding) {
            btnDelete.setOnClickListener {
                imageViewModel.obtainEvent(ImagesStateScreen.ImageEvent.OpenConfirmationDialog)
            }
            containerDocument.setOnClickListener {
                imageViewModel.obtainEvent(ImagesStateScreen.ImageEvent.OpenDuplicatesFile)
            }
        }
    }

}