package com.smart.cleaner.phoneclean.presentation.ui.duplicates_files

import androidx.lifecycle.viewModelScope
import com.smart.cleaner.phoneclean.domain.models.File
import com.smart.cleaner.phoneclean.domain.use_case.files.DuplicateFilesUseCase
import com.smart.cleaner.phoneclean.presentation.adapters.models.ChildFileItem
import com.smart.cleaner.phoneclean.presentation.adapters.models.ParentFileItem
import com.smart.cleaner.phoneclean.presentation.ui.base.BaseViewModel
import com.smart.cleaner.phoneclean.presentation.ui.models.FilesStateScreen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DuplicateFilesViewModel @Inject constructor(
    private val useCase: DuplicateFilesUseCase
): BaseViewModel<FilesStateScreen>(FilesStateScreen()) {

    init {
        getDuplicates()
    }

    private fun getDuplicates() {
        viewModelScope.launch(Dispatchers.IO) {
            val duplicates = useCase.getFileDuplicates()
            updateState {
                it.copy(
                    duplicates = map(duplicates)
                )
            }
        }
    }

    fun obtainEvent(event: FilesStateScreen.FileEvent) {
        when (event) {
            is FilesStateScreen.FileEvent.SelectFile -> selectFile(event.file, event.isSelected)
            is FilesStateScreen.FileEvent.SelectAll -> selectAll(event.duplicates, event.isSelected)
            else -> {}
        }
    }

    private fun selectFile(selectedImage: ChildFileItem, isSelected: Boolean) {
        val updatedList = mutableListOf<ParentFileItem>()
        screenState.value.duplicates.forEach { oldList ->
            if (oldList.files.contains(selectedImage)) {
                updatedList.add(
                    ParentFileItem(
                        count = oldList.count,
                        isAllSelected = isAllSelected(oldList, selectedImage, isSelected),
                        files = updateSelectedFileInList(oldList, selectedImage, isSelected)
                    )
                )
            } else {
                updatedList.add(oldList)
            }
        }
        updateList(updatedList)
        updateTotalFileSize()
    }

    private fun updateSelectedFileInList(
        oldList: ParentFileItem,
        selectedImage: ChildFileItem,
        isSelected: Boolean
    ) = oldList.files.map { file ->
        if (file == selectedImage) {
            ChildFileItem(isSelected = isSelected, filePath = file.filePath, size = file.size, fileName = file.fileName)
        } else {
            file
        }
    }

    private fun isAllSelected(
        oldList: ParentFileItem,
        selectedImage: ChildFileItem,
        isSelected: Boolean
    ): Boolean = oldList.isAllSelected && isSelected || oldList.files.none {
        if (it == selectedImage) {
            !isSelected
        } else {
            !it.isSelected
        }
    }

    private fun selectAll(newList: ParentFileItem, isSelected: Boolean) {
        val updatedList = mutableListOf<ParentFileItem>()
        screenState.value.duplicates.forEach { oldList ->
            if (oldList == newList) {
                updatedList.add(
                    ParentFileItem(
                        count = oldList.count,
                        isAllSelected = isSelected,
                        files = updateAllImagesInList(oldList, isSelected)
                    )
                )
            } else {
                updatedList.add(oldList)
            }
        }
        updateList(updatedList)
//        isCanDelete()
        updateTotalFileSize()
    }

    private fun updateList(duplicates: List<ParentFileItem>) {
        updateState {
            it.copy(
                duplicates = duplicates
            )
        }
    }

    private fun updateTotalFileSize() {
        var totalSize = 0L
        screenState.value.duplicates.forEach { duplicates ->
            duplicates.files.forEach { image ->
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

    private fun updateAllImagesInList(
        oldList: ParentFileItem,
        isSelected: Boolean
    ) = oldList.files.map {
        ChildFileItem(
            isSelected = isSelected,
            filePath = it.filePath,
            size = it.size,
            fileName = it.fileName
        )
    }

    private fun map(list: List<List<File>>): List<ParentFileItem> {
        return list.map { duplicates ->
            ParentFileItem(
                count = duplicates.size,
                isAllSelected = false,
                files = duplicates.map { ChildFileItem(
                    isSelected = false,
                    filePath = it.path,
                    fileName = it.name,
                    size = it.size
                ) }
            )
        }
    }

}