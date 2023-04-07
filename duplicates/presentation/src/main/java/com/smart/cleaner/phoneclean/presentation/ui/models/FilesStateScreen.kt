package com.smart.cleaner.phoneclean.presentation.ui.models

import com.smart.cleaner.phoneclean.presentation.adapters.models.ChildFileItem
import com.smart.cleaner.phoneclean.presentation.adapters.models.ParentFileItem

data class FilesStateScreen(
    val totalSize: Long = 0L,
    val isLoading: Boolean = true,
    val isNotFound: Boolean = false,
    val hasPermission: Boolean = false,
    val event: FileEvent = FileEvent.Default,
    val duplicates: List<ParentFileItem> = emptyList(),
) {

    sealed class FileEvent() {

        object Default : FileEvent()

        object CheckPermission: FileEvent()

        object CancelPermissionDialog: FileEvent()

        object OpenPermissionDialog : FileEvent()

        object OpenConfirmationDialog : FileEvent()

        object OpenDuplicatesImages : FileEvent()

        class Delete() : FileEvent()

        class DeleteDone() : FileEvent()

        class SelectAll(
            val duplicates: ParentFileItem,
            val isSelected: Boolean
        ) : FileEvent()

        class SelectFile(
            val file: ChildFileItem,
            val isSelected: Boolean
        ) : FileEvent()

    }

}