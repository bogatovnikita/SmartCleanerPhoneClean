package yin_kio.garbage_clean.domain.use_cases

import yin_kio.garbage_clean.domain.entities.GarbageSelector
import yin_kio.garbage_clean.domain.services.garbage_forms_provider.GarbageFormsProvider
import yin_kio.garbage_clean.domain.ui_out.garbage_out_creator.GarbageOutCreator
import yin_kio.garbage_clean.domain.ui_out.UiOuter

class UpdateUseCase(
    private val uiOuter: UiOuter,
    private val garbageSelector: GarbageSelector,
    private val garbageFormsProvider: GarbageFormsProvider,
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