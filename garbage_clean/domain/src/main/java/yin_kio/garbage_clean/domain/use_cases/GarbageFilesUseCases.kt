package yin_kio.garbage_clean.domain.use_cases

import yin_kio.garbage_clean.domain.entities.GarbageSelector
import yin_kio.garbage_clean.domain.gateways.Permissions
import yin_kio.garbage_clean.domain.ui_out.Checkable
import yin_kio.garbage_clean.domain.ui_out.UiOuter

class GarbageFilesUseCases(
    private val uiOuter: UiOuter,
    private val garbageSelector: GarbageSelector,
    private val permissions: Permissions
) {

    fun closePermissionDialog(){
        uiOuter.closePermissionDialog()
    }

    fun requestPermission(){
        uiOuter.requestPermission()
    }

    fun switchItemSelection(groupIndex: Int, itemIndex: Int, itemCheckable: Checkable){
        garbageSelector.switchItemSelection(groupIndex, itemIndex)
        itemCheckable.setChecked(garbageSelector.isItemSelected(groupIndex, itemIndex))
        uiOuter.updateGroup(groupIndex)
    }

    fun switchGroupSelection(groupIndex: Int, groupCheckable: Checkable){
        garbageSelector.switchGroupSelected(groupIndex)
        groupCheckable.setChecked(garbageSelector.isGroupSelected(groupIndex))
        uiOuter.updateGroup(groupIndex)

    }

    fun scan(){
        if (permissions.hasPermission){
            uiOuter.outGarbage(garbageSelector.getGarbage())
        } else {
            uiOuter.showPermissionDialog()
        }
    }

}