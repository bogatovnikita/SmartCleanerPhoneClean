package com.smart.cleaner.phoneclean.presentation.ui.duplicate_images

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.smart.cleaner.phoneclean.presentation.R
import com.smart.cleaner.phoneclean.presentation.adapters.DuplicatesImagesAdapter
import com.smart.cleaner.phoneclean.presentation.adapters.listeners.OnChangeSelectListener
import com.smart.cleaner.phoneclean.presentation.adapters.models.ChildImageItem
import com.smart.cleaner.phoneclean.presentation.databinding.FragmentDuplicateImagesBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DuplicateImagesFragment : Fragment(R.layout.fragment_duplicate_images) {

    private val viewModel: DuplicateImagesViewModel by viewModels()

    private val binding: FragmentDuplicateImagesBinding by viewBinding()

    private val adapter: DuplicatesImagesAdapter =
        DuplicatesImagesAdapter(object : OnChangeSelectListener {
            override fun selectAll(duplicates: List<ChildImageItem>, isSelected: Boolean) {
                viewModel.obtainEvent(
                    ImagesStateScreen.ImageEvent.SelectAll(
                        duplicates,
                        isSelected
                    )
                )
            }

            override fun selectImage(image: ChildImageItem, isSelected: Boolean) {
                viewModel.obtainEvent(ImagesStateScreen.ImageEvent.SelectImage(image, isSelected))
            }

        })

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapter()
        initObserverScreenState()
    }

    private fun initObserverScreenState() {
        lifecycleScope.launchWhenCreated {
            viewModel.screenState.collect { state ->
                adapter.submitList(state.duplicates)
                navigate(state.event)
                render(state)
            }
        }
    }

    private fun render(state: ImagesStateScreen) {
        with(binding) {
            groupStartLoading.isVisible = state.isLoading
            groupStopLoading.isVisible = !state.isLoading
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
            is ImagesStateScreen.ImageEvent.OpenPermissionDialog -> findNavController().navigate(R.id.to_requestStoragePermDialog)
            else -> {}
        }

    }

}