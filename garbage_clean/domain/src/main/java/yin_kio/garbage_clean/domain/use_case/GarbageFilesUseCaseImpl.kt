package yin_kio.garbage_clean.domain.use_case

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import yin_kio.garbage_clean.domain.actions.CleanAction
import yin_kio.garbage_clean.domain.actions.ScanAction
import yin_kio.garbage_clean.domain.actions.UpdateAction
import yin_kio.garbage_clean.domain.entities.GarbageSelector
import yin_kio.garbage_clean.domain.gateways.Permissions
import yin_kio.garbage_clean.domain.gateways.StorageInfo
import yin_kio.garbage_clean.domain.services.CleanTracker
import yin_kio.garbage_clean.domain.services.garbage_files.GarbageType
import yin_kio.garbage_clean.domain.ui_out.Checkable
import yin_kio.garbage_clean.domain.ui_out.UiOut
import yin_kio.garbage_clean.domain.ui_out.UiOuter
import yin_kio.garbage_clean.domain.ui_out.garbage_out_creator.GarbageOutCreator
import java.io.File
import kotlin.coroutines.CoroutineContext

internal class GarbageFilesUseCaseImpl(
    private val uiOuter: UiOuter,
    private val garbageSelector: GarbageSelector,
    private val garbageOutCreator: GarbageOutCreator,
    private val permissions: Permissions,
    private val updateAction: UpdateAction,
    private val storageInfo: StorageInfo,
    private val coroutineScope: CoroutineScope,
    private val dispatcher: CoroutineContext,
    private val cleanAction: CleanAction,
    private val scanAction: ScanAction,
    private val updateState: UpdateStateHolder,
    private val cleanTracker: CleanTracker
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

    override fun scanOrClean() = async {
        if (updateState.updateState == UpdateState.Successful){
            cleanAction.clean()
        } else {
            scanAction.scan()
        }
    }

    override fun update() = async{
        val hasPermission = permissions.hasPermission

        if (hasPermission){
            updateAction.update()
        } else {
            uiOuter.showPermissionRequired()
            uiOuter.showAttentionMessagesColors()
            uiOuter.showPermissionDialog()
        }

    }

    override fun checkPermissionAndLanguage() {
        if (permissions.hasPermission){
            uiOuter.hidePermissionRequired()
        } else {
            uiOuter.showPermissionRequired()
        }
        uiOuter.updageLanguage(
            updateState.updateState,
            garbageOutCreator.create(
                garbageSelector.getGarbage()),
            cleanTracker.isCleaned
        )
    }

    override fun closeInter(){
        uiOuter.showResult(storageInfo.freedVolume)
    }


    override fun start() = async {
        if (permissions.hasPermission){
            updateAction.update()
        } else {
            uiOuter.out(UiOut.StartWithoutPermission)
        }
    }

    override fun updateLanguage() {
        uiOuter.changeLanguage(garbageSelector.uiOut)
    }

    private fun async(block: suspend () -> Unit){
        coroutineScope.launch(dispatcher) { block() }
    }

}