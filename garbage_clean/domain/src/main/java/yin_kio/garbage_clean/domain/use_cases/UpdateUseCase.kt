package yin_kio.garbage_clean.domain.use_cases

import kotlinx.coroutines.delay
import yin_kio.garbage_clean.domain.entities.GarbageSelector
import yin_kio.garbage_clean.domain.services.garbage_forms_provider.GarbageFormsProvider
import yin_kio.garbage_clean.domain.ui_out.UiOuter
import yin_kio.garbage_clean.domain.ui_out.garbage_out_creator.GarbageOutCreator

internal class UpdateUseCase(
    private val uiOuter: UiOuter,
    private val garbageSelector: GarbageSelector,
    private val garbageFormsProvider: GarbageFormsProvider,
    private val garbageOutCreator: GarbageOutCreator,
    private val updateState: UpdateStateHolder
) {

    suspend fun update() {
        if (updateState.updateState == UpdateState.Progress) return
        updateState.updateState = UpdateState.Progress

        uiOuter.showUpdateProgress()


        val forms = garbageFormsProvider.provide()


        garbageSelector.setGarbage(forms)

        val garbage = garbageSelector.getGarbage()
        val garbageOut = garbageOutCreator.create(garbage)
        uiOuter.outGarbage(garbageOut)
        updateState.updateState = UpdateState.Successful
    }

}