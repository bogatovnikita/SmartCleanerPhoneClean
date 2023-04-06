package com.smart.cleaner.phoneclean.presentation.ui.duplicates_files

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.smart.cleaner.phoneclean.presentation.R
import com.smart.cleaner.phoneclean.presentation.adapters.DuplicatesFilesParentAdapter
import com.smart.cleaner.phoneclean.presentation.adapters.DuplicatesImagesParentAdapter
import com.smart.cleaner.phoneclean.presentation.adapters.listeners.OnFileChangeSelectListener
import com.smart.cleaner.phoneclean.presentation.adapters.listeners.OnImageChangeSelectListener
import com.smart.cleaner.phoneclean.presentation.adapters.models.ChildFileItem
import com.smart.cleaner.phoneclean.presentation.adapters.models.ChildImageItem
import com.smart.cleaner.phoneclean.presentation.adapters.models.ParentFileItem
import com.smart.cleaner.phoneclean.presentation.adapters.models.ParentImageItem
import com.smart.cleaner.phoneclean.presentation.databinding.FragmentDuplicateFilesBinding
import com.smart.cleaner.phoneclean.presentation.ui.models.ImagesStateScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DuplicateFilesFragment : Fragment(R.layout.fragment_duplicate_files) {

    private val binding: FragmentDuplicateFilesBinding by viewBinding()

    private val viewModel: DuplicateFilesViewModel by viewModels()

    private val adapter: DuplicatesFilesParentAdapter =
        DuplicatesFilesParentAdapter(object : OnFileChangeSelectListener {
            override fun selectAll(duplicates: ParentFileItem, isSelected: Boolean) {
                TODO("Not yet implemented")
            }

            override fun selectFile(image: ChildFileItem, isSelected: Boolean) {
                TODO("Not yet implemented")
            }

        })

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapter()
        initObserverScreenState()
    }

    private fun initObserverScreenState() {
        lifecycleScope.launchWhenResumed {
            viewModel.screenState.collect { state ->
                adapter.submitList(state.duplicates)
            }
        }
    }

    private fun initAdapter() {
        binding.recyclerView.adapter = adapter
        val layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.recyclerView.layoutManager = layoutManager
    }
}