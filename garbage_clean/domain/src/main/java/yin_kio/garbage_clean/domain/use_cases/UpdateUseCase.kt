package yin_kio.garbage_clean.domain.use_cases

import yin_kio.garbage_clean.domain.entities.GarbageSelector
import yin_kio.garbage_clean.domain.services.GarbageFormsCreator
import yin_kio.garbage_clean.domain.ui_out.GarbageOutCreator
import yin_kio.garbage_clean.domain.ui_out.UiOuter

class UpdateUseCase(
    private val uiOuter: UiOuter,
    private val garbageSelector: GarbageSelector,
    private val garbageFormsProvider: GarbageFormsCreator,
    private val garbageOutCreator: GarbageOutCreator,
) {

    fun update(){
        uiOuter.showUpdateProgress()

        val forms = garbageFormsProvider.provide()

        garbageSelector.setGarbage(forms)

        val garbage = garbageSelector.getGarbage()
        val garbageOut = garbageOutCreator.create(garbage)
        uiOuter.outGarbage(garbageOut)
    }

}