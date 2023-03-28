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

    private fun getDuplicates() {
        viewModelScope.launch(Dispatchers.IO) {
            val duplicates = useCase.getImageDuplicates()
            updateState {
                it.copy(
                    duplicates = mapTo(duplicates),
                    isLoading = false,
                    isNotFound = duplicates.isEmpty(),
                    isCanDelete = false,
                )
            }
        }
    }

    fun obtainEvent(event: ImagesStateScreen.ImageEvent) {
        when (event) {
            is ImagesStateScreen.ImageEvent.SelectImage -> selectImage(
                event.image,
                event.isSelected
            )
            is ImagesStateScreen.ImageEvent.SelectAll -> selectAll(
                event.duplicates,
                event.isSelected
            )
            is ImagesStateScreen.ImageEvent.Default -> setEvent(event)
            is ImagesStateScreen.ImageEvent.OpenFilesDuplicates -> {}
            is ImagesStateScreen.ImageEvent.OpenConfirmationDialog -> setEvent(event)
            is ImagesStateScreen.ImageEvent.OpenPermissionDialog -> {}
            is ImagesStateScreen.ImageEvent.CheckPermission -> checkPermission()
            is ImagesStateScreen.ImageEvent.CancelPermissionDialog -> cancelPermissionDialog()
        }
    }

    private fun selectAll(newList: ParentImageItem, isSelected: Boolean) {
        val updatedList = mutableListOf<ParentImageItem>()
        screenState.value.duplicates.forEach { oldList ->
            if (oldList == newList) {
                updatedList.add(
                    ParentImageItem(
                        count = oldList.count,
                        isAllSelected = isSelected,
                        images = updateAllImagesInList(oldList, isSelected)
                    )
                )
            } else {
                updatedList.add(oldList)
            }
        }
        updateList(updatedList)
        isCanDelete()
    }

    private fun selectImage(selectedImage: ChildImageItem, isSelected: Boolean) {
        val updatedList = mutableListOf<ParentImageItem>()
        screenState.value.duplicates.forEach { oldList ->
            if (oldList.images.contains(selectedImage)) {
                updatedList.add(
                    ParentImageItem(
                        count = oldList.count,
                        isAllSelected = isAllSelected(oldList, selectedImage, isSelected),
                        images = updateSelectedImageInList(oldList, selectedImage, isSelected)
                    )
                )
            } else {
                updatedList.add(oldList)
            }
        }
        updateList(updatedList)
        isCanDelete()
    }

    private fun checkPermission() {
        val hasPerm = useCase.hasStoragePermissions()
        var isLoading = false
        if (hasPerm) {
            getDuplicates()
            isLoading = true && screenState.value.duplicates.isEmpty()
        } else {
            setEvent(ImagesStateScreen.ImageEvent.OpenPermissionDialog)
        }
        updateState {
            it.copy(
                hasPermission = hasPerm,
                isLoading = isLoading,
                isNotFound = false
            )
        }
    }

    private fun setEvent(event: ImagesStateScreen.ImageEvent) {
        updateState {
            it.copy(
                event = event
            )
        }
    }

    private fun updateList(list: List<ParentImageItem>) {
        updateState {
            it.copy(
                duplicates = list
            )
        }
    }

    private fun mapTo(duplicates: List<List<ImageInfo>>): List<ParentImageItem> {
        return duplicates.map { groupDuplicates ->
            ParentImageItem(
                count = groupDuplicates.size,
                images = groupDuplicates.map { ChildImageItem(imagePath = it.path) }
            )
        }
    }

    private fun isAllSelected(
        oldList: ParentImageItem,
        selectedImage: ChildImageItem,
        isSelected: Boolean
    ): Boolean = oldList.isAllSelected && isSelected || oldList.images.none {
        if (it == selectedImage) {
            !isSelected
        } else {
            !it.isSelected
        }
    }

    private fun updateSelectedImageInList(
        oldList: ParentImageItem,
        selectedImage: ChildImageItem,
        isSelected: Boolean
    ) = oldList.images.map { image ->
        if (image == selectedImage) {
            ChildImageItem(isSelected = isSelected, imagePath = image.imagePath)
        } else {
            image
        }
    }

    private fun updateAllImagesInList(
        oldList: ParentImageItem,
        isSelected: Boolean
    ) = oldList.images.map {
        ChildImageItem(
            isSelected = isSelected,
            imagePath = it.imagePath
        )
    }

    private fun isCanDelete() {
        var isCanDelete = false
        if (screenState.value.duplicates.any { it.isAllSelected }) {
            isCanDelete = true
        } else {
            screenState.value.duplicates.forEach {images ->
                if (images.images.any { it.isSelected }) {
                    isCanDelete = true
                    return@forEach
                }
            }
        }
        updateState {
            it.copy(
                isCanDelete = isCanDelete
            )
        }
    }

    private fun cancelPermissionDialog() {
        updateState {
            it.copy(
                isNotFound = true
            )
        }
    }

}