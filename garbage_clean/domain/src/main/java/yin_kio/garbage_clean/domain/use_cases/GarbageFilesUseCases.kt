package yin_kio.garbage_clean.domain.use_cases

import yin_kio.garbage_clean.domain.entities.GarbageSelector
import yin_kio.garbage_clean.domain.gateways.Permissions
import yin_kio.garbage_clean.domain.services.GarbageFormsCreator
import yin_kio.garbage_clean.domain.services.garbage_files.GarbageType
import yin_kio.garbage_clean.domain.ui_out.Checkable
import yin_kio.garbage_clean.domain.ui_out.GarbageOutCreator
import yin_kio.garbage_clean.domain.ui_out.UiOuter
import java.io.File

class GarbageFilesUseCases(
    private val uiOuter: UiOuter,
    private val garbageSelector: GarbageSelector,
    private val permissions: Permissions,
    private val garbageFormsCreator: GarbageFormsCreator,
    private val garbageOutCreator: GarbageOutCreator
) {

    fun closePermissionDialog(){
        uiOuter.closePermissionDialog()
    }

    fun requestPermission(){
        uiOuter.requestPermission()
    }

    fun switchItemSelection(group: GarbageType, file: File, itemCheckable: Checkable){
        garbageSelector.switchFileSelection(group, file)
        itemCheckable.setChecked(garbageSelector.isItemSelected(group, file))
        uiOuter.updateGroup(group)
    }

    fun switchGroupSelection(group: GarbageType, groupCheckable: Checkable){
        garbageSelector.switchGroupSelected(group)
        groupCheckable.setChecked(garbageSelector.isGroupSelected(group))
        uiOuter.updateGroup(group)

    }

    fun scan(){
        if (permissions.hasPermission){
            uiOuter.showUpdateProgress()

            val forms = garbageFormsCreator.provide()

            garbageSelector.setGarbage(forms)

            val garbage = garbageSelector.getGarbage()
            val garbageOut = garbageOutCreator.create(garbage)
            uiOuter.outGarbage(garbageOut)
        } else {
            uiOuter.showPermissionDialog()
        }
    }

}