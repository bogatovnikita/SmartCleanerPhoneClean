package com.smart.cleaner.phoneclean.presentation.ui.duplicate_images

import com.smart.cleaner.phoneclean.presentation.adapters.models.ChildImageItem
import com.smart.cleaner.phoneclean.presentation.adapters.models.ParentImageItem

data class ImagesStateScreen(
    val isLoading: Boolean = true,
    val isNotFound: Boolean = false,
    val isCanDelete: Boolean = false,
    val event: ImageEvent = ImageEvent.CheckPermission,
    val duplicates: List<ParentImageItem> = emptyList(),
) {

    sealed class ImageEvent() {

        object Default : ImageEvent()

        object CheckPermission: ImageEvent()

        object OpenPermissionDialog : ImageEvent()

        object OpenConfirmationDialog : ImageEvent()

        object OpenFilesDuplicates : ImageEvent()

        object Delete : ImageEvent()
        class SelectAll(
            private val duplicates: List<ChildImageItem>,
            private val isSelected: Boolean
        ) : ImageEvent()

        class SelectImage(
            private val image: ChildImageItem,
            private val isSelected: Boolean
        ) : ImageEvent()

    }

}