package com.smart.cleaner.phoneclean.presentation.ui.duplicate_images

import androidx.lifecycle.viewModelScope
import com.smart.cleaner.phoneclean.domain.models.ImageInfo
import com.smart.cleaner.phoneclean.domain.use_case.images.DuplicateImagesUseCase
import com.smart.cleaner.phoneclean.presentation.adapters.models.ChildImageItem
import com.smart.cleaner.phoneclean.presentation.adapters.models.ParentImageItem
import com.smart.cleaner.phoneclean.presentation.ui.base.BaseViewModel
import com.smart.cleaner.phoneclean.presentation.ui.models.ImagesStateScreen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class DuplicateImagesViewModel @Inject constructor(
    private val useCase: DuplicateImagesUseCase,
) : BaseViewModel<ImagesStateScreen>(ImagesStateScreen()) {

    private fun getDuplicates() {
        viewModelScope.launch(Dispatchers.IO) {
            val duplicates = useCase.getImageDuplicates()
            withContext(Dispatchers.Main) {
                updateState {
                    it.copy(
                        duplicates = mapUiModel(duplicates),
                        isLoading = false,
                        isNotFound = duplicates.isEmpty(),
                        isCanDelete = false,
                    )
                }
                isCanDelete()
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
            is ImagesStateScreen.ImageEvent.OpenConfirmationDialog -> setEvent(event)
            is ImagesStateScreen.ImageEvent.CheckPermission -> checkPermission()
            is ImagesStateScreen.ImageEvent.CancelPermissionDialog -> cancelPermissionDialog()
            is ImagesStateScreen.ImageEvent.ConfirmedImageDeletion -> setEvent(event)
            is ImagesStateScreen.ImageEvent.Delete -> saveTimeAndDelete(event.time)
            is ImagesStateScreen.ImageEvent.DeleteDone -> updateListAfterDeleting()
            else -> {}
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
        updateTotalImageSize()
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
        updateTotalImageSize()
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
            ChildImageItem(isSelected = isSelected, imagePath = image.imagePath, size = image.size)
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
            imagePath = it.imagePath,
            size = it.size
        )
    }

    private fun isCanDelete() {
        var isCanDelete = false
        if (screenState.value.duplicates.any { it.isAllSelected }) {
            isCanDelete = true
        } else {
            screenState.value.duplicates.forEach { images ->
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

    private fun saveTimeAndDelete(time: Long) {
        useCase.saveDuplicatesDeleteTime()
        updateState {
            it.copy(
                timeDeletion = time
            )
        }
        viewModelScope.launch {
            useCase.deleteDuplicates(getListForDelete())
        }
    }

    private fun updateTotalImageSize() {
        var totalSize = 0L
        screenState.value.duplicates.forEach { duplicates ->
            duplicates.images.forEach { image ->
                if (image.isSelected)
                    totalSize += image.size
            }
        }
        updateState {
            it.copy(
                totalSize = totalSize
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

    private fun mapUiModel(duplicates: List<List<ImageInfo>>): List<ParentImageItem> {
        return duplicates.map { groupDuplicates ->
            val images = groupDuplicates.map { imageInfo ->
                var isSelected = false
                screenState.value.duplicates.forEach { oldList ->
                    oldList.images.forEach { image ->
                        if (image.imagePath == imageInfo.path) {
                            isSelected = image.isSelected
                            return@forEach
                        }
                    }
                    if (isSelected) {
                        return@forEach
                    }
                }
                ChildImageItem(
                    imagePath = imageInfo.path,
                    isSelected = isSelected,
                    size = imageInfo.size
                )
            }
            ParentImageItem(
                isAllSelected = images.none { !it.isSelected },
                count = groupDuplicates.size,
                images = images
            )
        }
    }

    private fun getListForDelete(): List<ImageInfo> {
        val listForDelete = mutableListOf<ImageInfo>()
        screenState.value.duplicates.forEach { duplicates ->
            duplicates.images.forEach { image ->
                if (image.isSelected) {
                    listForDelete.add(ImageInfo(image.imagePath, 0))
                }
            }
        }
        return listForDelete
    }

    private fun updateListAfterDeleting() {
        viewModelScope.launch(Dispatchers.Default) {
            val updatedList = mutableListOf<ParentImageItem>()
            screenState.value.duplicates.forEach { duplicates ->
                updatedList.add(
                    ParentImageItem(
                        count = duplicates.images.filter { !it.isSelected }.size,
                        isAllSelected = duplicates.images.none { !it.isSelected },
                        images = duplicates.images.filter { !it.isSelected },
                    )
                )
            }
            updateState {
                it.copy(
                    duplicates = updatedList
                )
            }
        }
    }

}