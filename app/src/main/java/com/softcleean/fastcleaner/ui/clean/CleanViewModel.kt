package com.softcleean.fastcleaner.ui.clean

import androidx.lifecycle.viewModelScope
import com.softcleean.fastcleaner.domain.clean.CleanUseCase
import com.softcleean.fastcleaner.ssearch.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CleanViewModel @Inject constructor(
    private val cleanUseCase: CleanUseCase
): BaseViewModel<CleanStateScreen>(CleanStateScreen()) {

    fun getGarbageInfo() {
        viewModelScope.launch(Dispatchers.Default) {
            val usedMemory = toGb(cleanUseCase.getUsedSizeMemory())
            val totalMemory = toGb(cleanUseCase.getTotalSizeMemory())
            val freeMemory = totalMemory - usedMemory
            updateState {
                it.copy(
                    isCleared = cleanUseCase.isGarbageCleared(),
                    totalGarbageSize = cleanUseCase.getTotalGarbageSize(),
                    freeMemory = freeMemory,
                    memoryPercent = (100 - (freeMemory * 100 / totalMemory)).toInt(),
                )
            }
        }
    }

    fun cleanGarbage() = cleanUseCase.cleanGarbage()

    private fun toGb(size: Long): Double = size / 1024.0 / 1024.0 / 1024

}