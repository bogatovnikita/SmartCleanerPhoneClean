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