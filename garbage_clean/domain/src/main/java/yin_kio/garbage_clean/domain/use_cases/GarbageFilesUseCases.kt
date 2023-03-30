package yin_kio.garbage_clean.domain.use_cases

import yin_kio.garbage_clean.domain.ui_out.UiOuter

class GarbageFilesUseCases(
    private val uiOuter: UiOuter
) {

    fun closePermissionDialog(){
        uiOuter.closePermissionDialog()
    }

}