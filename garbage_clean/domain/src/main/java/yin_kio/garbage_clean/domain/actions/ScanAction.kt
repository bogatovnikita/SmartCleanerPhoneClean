package yin_kio.garbage_clean.domain.actions

import yin_kio.garbage_clean.domain.gateways.Permissions
import yin_kio.garbage_clean.domain.ui_out.UiOuter

internal class ScanAction(
    private val permissions: Permissions,
    private val updateAction: UpdateAction,
    private val uiOuter: UiOuter,
) {

    suspend fun scan(){
        if (permissions.hasPermission){
            updateAction.update()
        } else {
            uiOuter.showPermissionDialog()
        }
    }

}