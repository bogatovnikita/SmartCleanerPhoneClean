package yin_kio.garbage_clean.domain.use_case

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import yin_kio.garbage_clean.domain.actions.CleanAction
import yin_kio.garbage_clean.domain.actions.UpdateAction
import yin_kio.garbage_clean.domain.entities.GarbageSelector
import yin_kio.garbage_clean.domain.gateways.Permissions
import yin_kio.garbage_clean.domain.gateways.StorageInfo
import yin_kio.garbage_clean.domain.services.garbage_files.GarbageType
import yin_kio.garbage_clean.domain.ui_out.Checkable
import yin_kio.garbage_clean.domain.ui_out.UiOut
import yin_kio.garbage_clean.domain.ui_out.UiOuter
import java.io.File
import kotlin.coroutines.CoroutineContext

internal class GarbageFilesUseCaseImpl(
    private val uiOuter: UiOuter,
    private val garbageSelector: GarbageSelector,
    private val permissions: Permissions,
    private val updateAction: UpdateAction,
    private val storageInfo: StorageInfo,
    private val coroutineScope: CoroutineScope,
    private val dispatcher: CoroutineContext,
    private val cleanAction: CleanAction,
) : GarbageFilesUseCase {

    override fun closePermissionDialog(){
        uiOuter.closePermissionDialog()
    }

    override fun requestPermission(){
        uiOuter.requestPermission()
    }

    override fun switchItemSelection(group: GarbageType, file: File, itemCheckable: Checkable){
        if (!permissions.hasPermission) return

        garbageSelector.switchFileSelection(group, file)
        itemCheckable.setChecked(garbageSelector.isItemSelected(group, file))
        uiOuter.updateGroup(group, garbageSelector.hasSelected)
    }

    override fun switchGroupSelection(group: GarbageType, groupCheckable: Checkable){
        if (!permissions.hasPermission) return

        garbageSelector.switchGroupSelected(group)
        groupCheckable.setChecked(garbageSelector.isGroupSelected(group))
        uiOuter.updateChildrenAndGroup(group, garbageSelector.hasSelected)

    }

    override fun updateItemSelection(group: GarbageType, file: File, itemCheckable: Checkable) {
        itemCheckable.setChecked(garbageSelector.isItemSelected(group, file))
    }

    override fun updateGroupSelection(group: GarbageType, groupCheckable: Checkable) {
        groupCheckable.setChecked(garbageSelector.isGroupSelected(group))
    }


    override fun closeInter(){
        uiOuter.showResult(storageInfo.freedVolume)
    }


    override fun start() = async {
        println("!!! ${garbageSelector.uiOut}")
        when(garbageSelector.uiOut){
            UiOut.Init,
            UiOut.StartWithoutPermission -> updateOrShowPermissionRequired()
            is UiOut.Updated -> cleanAction.clean()
            else -> {}
        }
    }

    private suspend fun updateOrShowPermissionRequired() {
        if (permissions.hasPermission) {
            updateAction.update()
        } else {
            garbageSelector.uiOut = UiOut.StartWithoutPermission
            uiOuter.out(garbageSelector.uiOut)
        }
    }

    override fun updateLanguage() {
        uiOuter.changeLanguage(garbageSelector.uiOut)
    }

    private fun async(block: suspend () -> Unit){
        coroutineScope.launch(dispatcher) { block() }
    }

}