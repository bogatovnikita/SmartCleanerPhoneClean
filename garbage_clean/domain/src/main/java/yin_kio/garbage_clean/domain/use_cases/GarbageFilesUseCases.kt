package yin_kio.garbage_clean.domain.use_cases

import yin_kio.garbage_clean.domain.entities.Selector
import yin_kio.garbage_clean.domain.ui_out.Checkable
import yin_kio.garbage_clean.domain.ui_out.UiOuter

class GarbageFilesUseCases(
    private val uiOuter: UiOuter,
    private val selector: Selector
) {

    fun closePermissionDialog(){
        uiOuter.closePermissionDialog()
    }

    fun requestPermission(){
        uiOuter.requestPermission()
    }

    fun switchItemSelection(groupIndex: Int, itemIndex: Int, itemCheckable: Checkable){
        selector.switchItemSelection(groupIndex, itemIndex)
        itemCheckable.setChecked(selector.isItemSelected(groupIndex, itemIndex))
        uiOuter.updateGroup(groupIndex)
    }

    fun switchGroupSelection(groupIndex: Int, groupCheckable: Checkable){
        selector.switchGroupSelected(groupIndex)
        groupCheckable.setChecked(selector.isGroupSelected(groupIndex))
        uiOuter.updateGroup(groupIndex)

    }

}