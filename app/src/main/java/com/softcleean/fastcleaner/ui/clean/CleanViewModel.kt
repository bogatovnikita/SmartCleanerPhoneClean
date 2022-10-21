package com.softcleean.fastcleaner.ui.clean

import androidx.lifecycle.viewModelScope
import com.softcleean.fastcleaner.R
import com.softcleean.fastcleaner.adapters.ItemGarbage
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
                    garbageList = getGarbageList(),
                    totalGarbageSize = cleanUseCase.getTotalGarbageSize(),
                    freeMemory = freeMemory,
                    memoryPercent = (100 - (freeMemory * 100 / totalMemory)).toInt(),
                )
            }
        }
    }

    private fun getGarbageList(): List<ItemGarbage> {
        val isCleared = cleanUseCase.isGarbageCleared()
        val listGarbageSizeArray = cleanUseCase.getGarbageSizeArray()
        val listGarbageName = cleanUseCase.getGarbageNameArray(R.array.clean)
        val listGarbage = mutableListOf<ItemGarbage>()
        listGarbageName.forEachIndexed { index ,name ->
            listGarbage.add(ItemGarbage(isCleared, name, listGarbageSizeArray[index]))
        }
        return listGarbage
    }

    fun cleanGarbage() = cleanUseCase.cleanGarbage()

    private fun toGb(size: Long): Double = size / 1024.0 / 1024.0 / 1024

}