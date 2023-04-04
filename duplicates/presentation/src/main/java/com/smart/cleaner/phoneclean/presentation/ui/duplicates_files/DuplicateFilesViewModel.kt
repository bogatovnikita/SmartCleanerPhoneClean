package com.smart.cleaner.phoneclean.presentation.ui.duplicates_files

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.smart.cleaner.phoneclean.domain.use_case.files.DuplicateFilesUseCase
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
            Log.e("!!!", duplicates.toString())
        }
    }

}