package com.smart.cleaner.phoneclean.presentation.ui.duplicates_files

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.smart.cleaner.phoneclean.presentation.R
import com.smart.cleaner.phoneclean.presentation.databinding.FragmentDuplicateFilesBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DuplicateFilesFragment : Fragment(R.layout.fragment_duplicate_files) {

    private val binding: FragmentDuplicateFilesBinding by viewBinding()

    private val viewModel: DuplicateFilesViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding
        viewModel
    }
}