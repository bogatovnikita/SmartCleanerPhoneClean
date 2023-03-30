package com.smart.cleaner.phoneclean.presentation.ui.optimizing

import androidx.fragment.app.activityViewModels
import com.smart.cleaner.phoneclean.presentation.R
import com.smart.cleaner.phoneclean.presentation.adapters.models.ParentImageItem
import com.smart.cleaner.phoneclean.presentation.ui.duplicate_images.DuplicateImagesViewModel
import com.smart.cleaner.phoneclean.ui_core.adapters.base.BaseOptimizingFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OptimizingFragment : BaseOptimizingFragment(){

    private val imagesViewModel: DuplicateImagesViewModel by activityViewModels()

    override var nextScreenId = R.id.action_to_duplicateFilesFragment

    override fun getArrayOptimization() {
        listOptions = toListString(imagesViewModel.screenState.value.duplicates).toMutableList()
    }

    override fun startOptimizationFun() {

    }

    override fun getFunName(): String = getString(R.string.duplicates_deletion)

    private fun toListString(duplicates: List<ParentImageItem>): List<String> {
        val newList = mutableListOf<String>()
        duplicates.forEach { item ->
            item.images.forEach { image ->
                if (image.isSelected) {
                    newList.add(image.imagePath)
                }
            }
        }
        return newList
    }

}