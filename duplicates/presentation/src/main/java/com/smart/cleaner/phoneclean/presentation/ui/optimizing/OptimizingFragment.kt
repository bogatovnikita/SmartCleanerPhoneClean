package com.smart.cleaner.phoneclean.presentation.ui.optimizing

import androidx.fragment.app.activityViewModels
import com.smart.cleaner.phoneclean.presentation.R
import com.smart.cleaner.phoneclean.presentation.adapters.models.ParentImageItem
import com.smart.cleaner.phoneclean.presentation.ui.duplicate_images.DuplicateImagesViewModel
import com.smart.cleaner.phoneclean.ui_core.adapters.base.BaseOptimizingFragment
import com.smart.cleaner.phoneclean.ui_core.adapters.models.GeneralOptimizingItem
import com.smart.cleaner.phoneclean.ui_core.adapters.models.OptimizingItem
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OptimizingFragment : BaseOptimizingFragment(){

    private val imagesViewModel: DuplicateImagesViewModel by activityViewModels()

    override var nextScreenId = R.id.action_to_duplicatesResultFragment

    override fun getArrayOptimization() {
        listOptions = toGeneralOptimizingItemList(imagesViewModel.screenState.value.duplicates)
    }

    override fun startOptimizationFun() {

    }

    override fun getFunName(): String = getString(R.string.duplicates_deletion)

    private fun toGeneralOptimizingItemList(duplicates: List<ParentImageItem>): MutableList<OptimizingItem> {
        val newList = mutableListOf<OptimizingItem>()
        duplicates.forEach { item ->
            item.images.forEach { image ->
                if (image.isSelected) {
                    newList.add(GeneralOptimizingItem(image.imagePath))
                }
            }
        }
        return newList
    }

}