package com.smart.cleaner.phoneclean.presentation.ui.duplicate_images

import androidx.lifecycle.viewModelScope
import com.smart.cleaner.phoneclean.domain.models.ImageInfo
import com.smart.cleaner.phoneclean.domain.use_case.images.DuplicateImagesUseCase
import com.smart.cleaner.phoneclean.presentation.adapters.models.ChildImageItem
import com.smart.cleaner.phoneclean.presentation.adapters.models.ParentImageItem
import com.smart.cleaner.phoneclean.presentation.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DuplicateImagesViewModel @Inject constructor(
    private val useCase: DuplicateImagesUseCase,
) : BaseViewModel<ImagesStateScreen>(ImagesStateScreen()) {

    init {
        checkPermission()
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

    fun obtainEvent(event: ImagesStateScreen.ImageEvent) {
        when (event) {
            is ImagesStateScreen.ImageEvent.SelectImage -> {}
            is ImagesStateScreen.ImageEvent.SelectAll -> {}
            is ImagesStateScreen.ImageEvent.Default -> {}
            is ImagesStateScreen.ImageEvent.Delete -> {}
            is ImagesStateScreen.ImageEvent.OpenFilesDuplicates -> {}
            is ImagesStateScreen.ImageEvent.OpenConfirmationDialog -> {}
            is ImagesStateScreen.ImageEvent.OpenPermissionDialog -> {}
            is ImagesStateScreen.ImageEvent.CheckPermission -> {}
        }
    }

    private fun checkPermission() {
        if (useCase.hasStoragePermissions()) {
            getDuplicates()
        } else {
            setEvent(ImagesStateScreen.ImageEvent.OpenPermissionDialog)
        }
    }

    private fun setEvent(event: ImagesStateScreen.ImageEvent) {
        updateState {
            it.copy(
                event = event
            )
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