package com.smart.cleaner.phoneclean.presentation.ui.models

data class FilesStateScreen(
    val totalSize: Long = 0L,
    val timeDeletion: Long = 0,
    val isLoading: Boolean = true,
    val isNotFound: Boolean = false,
    val isCanDelete: Boolean = false,
    val hasPermission: Boolean = false,
    val event: FileEvent = FileEvent.Default,
    val duplicates: List<Any> = emptyList(),
) {

    sealed class FileEvent() {

        object Default : FileEvent()

        object CheckPermission: FileEvent()

        object CancelPermissionDialog: FileEvent()

        object OpenPermissionDialog : FileEvent()

        object OpenConfirmationDialog : FileEvent()

        object ConfirmedImageDeletion : FileEvent()

        class Delete(val time: Long) : FileEvent()

        class DeleteDone() : FileEvent()

        class SelectAll(
            val duplicates: Any,
            val isSelected: Boolean
        ) : FileEvent()

        class SelectFile(
            val file: Any,
            val isSelected: Boolean
        ) : FileEvent()

    }

}