package yin_kio.garbage_clean.domain.use_cases

import yin_kio.garbage_clean.domain.gateways.Permissions
import yin_kio.garbage_clean.domain.ui_out.UiOuter

internal class ScanUseCase(
    private val permissions: Permissions,
    private val updateUseCase: UpdateUseCase,
    private val uiOuter: UiOuter
) {

    suspend fun scan(){
        if (permissions.hasPermission){
            updateUseCase.update()
        } else {
            uiOuter.showPermissionDialog()
        }
    }

}