package yin_kio.garbage_clean.domain.use_cases

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import yin_kio.garbage_clean.domain.entities.GarbageSelector
import yin_kio.garbage_clean.domain.gateways.Permissions
import yin_kio.garbage_clean.domain.gateways.StorageInfo
import yin_kio.garbage_clean.domain.services.garbage_files.GarbageType
import yin_kio.garbage_clean.domain.ui_out.Checkable
import yin_kio.garbage_clean.domain.ui_out.UiOuter
import java.io.File
import kotlin.coroutines.CoroutineContext

internal class GarbageFilesUseCasesImpl(
    private val uiOuter: UiOuter,
    private val garbageSelector: GarbageSelector,
    private val permissions: Permissions,
    private val updateUseCase: UpdateUseCase,
    private val storageInfo: StorageInfo,
    private val coroutineScope: CoroutineScope,
    private val dispatcher: CoroutineContext,
    private val cleanUseCase: CleanUseCase,
    private val scanUseCase: ScanUseCase,
    private val updateState: UpdateStateHolder
) : GarbageFilesUseCases {

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
            cleanUseCase.clean()
        } else {
            scanUseCase.scan()
        }
    }

    override fun update() = async{

        if (permissions.hasPermission){
            updateUseCase.update()
        } else {
            uiOuter.showPermissionRequired()
        }

    }

    override fun closeInter(){
        uiOuter.showResult(storageInfo.freedVolume)
    }

    private fun async(block: suspend () -> Unit){
        coroutineScope.launch(dispatcher) { block() }
    }

}