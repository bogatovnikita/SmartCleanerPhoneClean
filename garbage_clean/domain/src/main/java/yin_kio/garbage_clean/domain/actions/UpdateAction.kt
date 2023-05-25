package yin_kio.garbage_clean.domain.actions

import yin_kio.garbage_clean.domain.entities.GarbageSelector
import yin_kio.garbage_clean.domain.services.CleanTracker
import yin_kio.garbage_clean.domain.services.garbage_forms_provider.GarbageFormsProvider
import yin_kio.garbage_clean.domain.ui_out.UiOut
import yin_kio.garbage_clean.domain.ui_out.UiOuter
import yin_kio.garbage_clean.domain.ui_out.garbage_out_creator.GarbageOutCreator
import yin_kio.garbage_clean.domain.use_case.UpdateState
import yin_kio.garbage_clean.domain.use_case.UpdateStateHolder

internal class UpdateAction(
    private val uiOuter: UiOuter,
    private val garbageSelector: GarbageSelector,
    private val garbageFormsProvider: GarbageFormsProvider,
    private val garbageOutCreator: GarbageOutCreator,
    private val updateState: UpdateStateHolder,
    private val cleanTracker: CleanTracker
) {

    suspend fun update() {
        if (updateState.updateState == UpdateState.Progress) return
        updateState.updateState = UpdateState.Progress
        val wasClean = cleanTracker.isCleaned

        garbageSelector.uiOut = UiOut.UpdateProgress(wasClean)
        uiOuter.out(garbageSelector.uiOut)

        val forms = garbageFormsProvider.provide()


        garbageSelector.setGarbage(forms)

        val garbage = garbageSelector.getGarbage()
        val garbageOut = garbageOutCreator.create(garbage)
        uiOuter.outGarbage(garbageOut, wasClean)
        updateState.updateState = UpdateState.Successful
    }

}