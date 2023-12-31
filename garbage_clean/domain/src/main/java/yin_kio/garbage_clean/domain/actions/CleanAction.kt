package yin_kio.garbage_clean.domain.actions

import kotlinx.coroutines.delay
import yin_kio.garbage_clean.domain.entities.GarbageSelector
import yin_kio.garbage_clean.domain.gateways.CleanTime
import yin_kio.garbage_clean.domain.gateways.Files
import yin_kio.garbage_clean.domain.gateways.StorageInfo
import yin_kio.garbage_clean.domain.ui_out.UiOut
import yin_kio.garbage_clean.domain.ui_out.UiOuter

internal class CleanAction(
    private val storageInfo: StorageInfo,
    private val uiOuter: UiOuter,
    private val files: Files,
    private val garbageSelector: GarbageSelector,
    private val cleanTime: CleanTime
) {

    suspend fun clean(){
        val selected = garbageSelector.getSelected()
        if (selected.isEmpty()) return

        storageInfo.saveStartVolume()

        uiOuter.showCleanProgress(selected)
        files.deleteFiles(selected)
        storageInfo.saveEndVolume()
        storageInfo.calculateFreedVolume()
        cleanTime.saveLastCleanTime()



        delay(4000)
        garbageSelector.uiOut = UiOut.UpdateProgress(true)
        uiOuter.out(garbageSelector.uiOut)
        delay(4000)


        uiOuter.showResult(storageInfo.freedVolume)
        garbageSelector.uiOut = UiOut.Init
    }

}