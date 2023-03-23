package com.smart.cleaner.phoneclean.presentation.ui.duplicate_images

import androidx.lifecycle.viewModelScope
import com.smart.cleaner.phoneclean.domain.models.ImageInfo
import com.smart.cleaner.phoneclean.domain.use_case.images.DuplicateImagesUseCaseImpl
import com.smart.cleaner.phoneclean.presentation.adapters.models.ChildImageItem
import com.smart.cleaner.phoneclean.presentation.adapters.models.ParentImageItem
import com.smart.cleaner.phoneclean.presentation.ui.base.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class DuplicateImagesViewModel @Inject constructor(
    private val useCase: DuplicateImagesUseCaseImpl,
) : BaseViewModel<ImagesStateScreen>(ImagesStateScreen()) {

    init {
        getDuplicates()
    }

    private fun getDuplicates() {
        viewModelScope.launch(Dispatchers.IO) {
            val duplicates = useCase.getImageDuplicates()
            updateState {
                it.copy(
                    duplicates = mapTo(duplicates),
                    isLoading = false,
                    isNotFound = duplicates.isEmpty()
                )
            }
        }
    }

    private fun mapTo(duplicates: List<List<ImageInfo>>): List<ParentImageItem> {
        return duplicates.map { groupDuplicates ->
            ParentImageItem(
                count = groupDuplicates.size,
                images = groupDuplicates.map { ChildImageItem(imagesPath = it.path) }
            )
        }
    }

}