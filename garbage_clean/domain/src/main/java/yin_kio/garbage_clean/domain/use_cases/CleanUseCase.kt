package yin_kio.garbage_clean.domain.use_cases

import yin_kio.garbage_clean.domain.entities.GarbageSelector
import yin_kio.garbage_clean.domain.gateways.Files
import yin_kio.garbage_clean.domain.gateways.StorageInfo
import yin_kio.garbage_clean.domain.ui_out.UiOuter

internal class CleanUseCase(
    private val storageInfo: StorageInfo,
    private val uiOuter: UiOuter,
    private val files: Files,
    private val garbageSelector: GarbageSelector
) {

    suspend fun clean(){
        val selected = garbageSelector.getSelected()
        if (selected.isEmpty()) return

        storageInfo.saveStartVolume()
        uiOuter.showCleanProgress(listOf())
        files.deleteFiles(selected)
        storageInfo.saveEndVolume()
        storageInfo.calculateEndVolume()
    }

}