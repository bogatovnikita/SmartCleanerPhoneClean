package com.smart.cleaner.phoneclean.presentation.ui.models

import com.smart.cleaner.phoneclean.presentation.adapters.models.ChildImageItem
import com.smart.cleaner.phoneclean.presentation.adapters.models.ParentImageItem

data class ImagesStateScreen(
    val totalSize: Long = 0L,
    val isLoading: Boolean = true,
    val isNotFound: Boolean = false,
    val hasPermission: Boolean = false,
    val event: ImageEvent = ImageEvent.CheckPermission,
    val duplicates: List<ParentImageItem> = emptyList(),
) {

    sealed class ImageEvent() {

        object Default : ImageEvent()

        object CheckPermission: ImageEvent()

        object CancelPermissionDialog: ImageEvent()

        object OpenPermissionDialog : ImageEvent()

        object OpenDuplicatesFile : ImageEvent()

        object OpenConfirmationDialog : ImageEvent()

        class Delete() : ImageEvent()

        class DeleteDone : ImageEvent()

        class SelectAll(
            val duplicates: ParentImageItem,
            val isSelected: Boolean
        ) : ImageEvent()

        class SelectImage(
            val image: ChildImageItem,
            val isSelected: Boolean
        ) : ImageEvent()

    }

}