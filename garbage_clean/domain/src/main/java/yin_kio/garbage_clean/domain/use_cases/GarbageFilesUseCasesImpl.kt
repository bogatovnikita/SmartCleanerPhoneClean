package yin_kio.garbage_clean.domain.use_cases

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import yin_kio.garbage_clean.domain.entities.GarbageSelector
import yin_kio.garbage_clean.domain.gateways.Files
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
    private val files: Files,
    private val coroutineScope: CoroutineScope,
    private val dispatcher: CoroutineContext,
    private val cleanUseCase: CleanUseCase
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
        uiOuter.updateGroup(group)
    }

    override fun switchGroupSelection(group: GarbageType, groupCheckable: Checkable){
        if (!permissions.hasPermission) return

        garbageSelector.switchGroupSelected(group)
        groupCheckable.setChecked(garbageSelector.isGroupSelected(group))
        uiOuter.updateGroup(group)

    }

    override fun scan() = async {
        if (permissions.hasPermission){
            updateUseCase.update()
        } else {
            uiOuter.showPermissionDialog()
        }
    }

    override fun start() = async{

        if (permissions.hasPermission){
            updateUseCase.update()
        } else {
            uiOuter.showPermissionRequired()
        }

    }

    override fun clean() = async {
        cleanUseCase.clean()
    }

    override fun closeInter(){
        uiOuter.showResult(storageInfo.freedVolume)
    }

    private fun async(block: suspend () -> Unit){
        coroutineScope.launch(dispatcher) { block() }
    }

}