package com.entertainment.event.ssearch.ar155.functions.clean

import androidx.lifecycle.viewModelScope
import com.entertainment.event.ssearch.ar155.R
import com.entertainment.event.ssearch.ar155.adapters.ItemGarbage
import com.entertainment.event.ssearch.domain.clean.CleanUseCase
import com.entertainment.event.ssearch.presentation.base.BaseViewModel
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
            updateState {
                it.copy(
                    isCleared = cleanUseCase.isGarbageCleared(),
                    garbageList = getGarbageList(),
                    totalGarbageSize = cleanUseCase.getTotalGarbageSize(),
                    totalSizeMemory = cleanUseCase.getTotalSizeMemory(),
                    usedSizeMemory = cleanUseCase.getUsedSizeMemory(),
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

}