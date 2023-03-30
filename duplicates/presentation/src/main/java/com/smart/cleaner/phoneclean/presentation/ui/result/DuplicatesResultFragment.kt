package com.smart.cleaner.phoneclean.presentation.ui.result

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.smart.cleaner.phoneclean.presentation.R
import com.smart.cleaner.phoneclean.presentation.databinding.FragmentDuplicatesResultBinding
import com.smart.cleaner.phoneclean.presentation.ui.duplicate_images.DuplicateImagesViewModel
import com.smart.cleaner.phoneclean.ui_core.adapters.base.BaseFragmentResult
import com.smart.cleaner.phoneclean.ui_core.adapters.models.OptimizingType
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DuplicatesResultFragment(
    override val typeResult: OptimizingType = OptimizingType.Duplicates
) : BaseFragmentResult(R.layout.fragment_duplicates_result) {

    private val binding: FragmentDuplicatesResultBinding by viewBinding()

    private val imagesViewModel: DuplicateImagesViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        renderState()
    }

    private fun renderState() {
        val imagesTimeDeletion = imagesViewModel.screenState.value.timeDeletion
        val filesTimeDeletion = 0L
        binding.tvSize.text =
            if (imagesTimeDeletion > filesTimeDeletion) toReadableSizeFormat(imagesViewModel.screenState.value.totalSize)
            else toReadableSizeFormat(filesTimeDeletion)
    }

    private fun toReadableSizeFormat(size: Long): String {
        return  ""
    }

    override fun setRecyclerView(): RecyclerView = binding.recyclerView

}