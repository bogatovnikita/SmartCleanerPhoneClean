package com.smart.cleaner.phoneclean.presentation.ui.duplicate_images

import com.smart.cleaner.phoneclean.presentation.adapters.models.ParentImageItem

data class ImagesStateScreen(
    val isLoading: Boolean = true,
    val isNotFound: Boolean = false,
    val isCanDelete: Boolean = false,
    val event: ImageEvent = ImageEvent.Default,
    val duplicates: List<ParentImageItem> = emptyList(),
) {

    sealed class ImageEvent() {

        object Default: ImageEvent()

        object OpenPermissionDialog: ImageEvent()

        object OpenConfirmationDialog: ImageEvent()

        object OpenFilesDuplicates: ImageEvent()

        object Delete: ImageEvent()

    }

}