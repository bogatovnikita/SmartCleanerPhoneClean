package com.smart.cleaner.phoneclean.presentation.ui.optimizing

import androidx.fragment.app.activityViewModels
import com.smart.cleaner.phoneclean.presentation.R
import com.smart.cleaner.phoneclean.presentation.adapters.models.ParentFileItem
import com.smart.cleaner.phoneclean.presentation.adapters.models.ParentImageItem
import com.smart.cleaner.phoneclean.presentation.ui.duplicate_images.DuplicateImagesViewModel
import com.smart.cleaner.phoneclean.presentation.ui.duplicates_files.DuplicateFilesViewModel
import com.smart.cleaner.phoneclean.ui_core.adapters.base.BaseOptimizingFragment
import com.smart.cleaner.phoneclean.ui_core.adapters.models.GeneralOptimizingItem
import com.smart.cleaner.phoneclean.ui_core.adapters.models.OptimizingItem
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OptimizingFragment : BaseOptimizingFragment() {

    private val imagesViewModel: DuplicateImagesViewModel by activityViewModels()

    private val filesViewModel: DuplicateFilesViewModel by activityViewModels()

    override var nextScreenId = R.id.action_to_duplicatesResultFragment

    override fun getArrayOptimization() {
        listOptions = toGeneralOptimizingItemList(
            imagesViewModel.screenState.value.duplicates,
            filesViewModel.screenState.value.duplicates
        )
    }

    override fun startOptimizationFun() {}

    override fun getFunName(): String = getString(R.string.duplicates_deletion)

    private fun toGeneralOptimizingItemList(
        duplicatesImage: List<ParentImageItem>,
        duplicatesFile: List<ParentFileItem>
    ): MutableList<OptimizingItem> {
        val newList = mutableListOf<OptimizingItem>()
        duplicatesImage.forEach { item ->
            item.images.forEach { image ->
                if (image.isSelected) {
                    newList.add(GeneralOptimizingItem(image.imagePath))
                }
            }
        }
        duplicatesFile.forEach { item ->
            item.files.forEach { file ->
                if (file.isSelected) {
                    newList.add(GeneralOptimizingItem(file.filePath))
                }
            }
        }
        return newList
    }

}