package yin_kio.garbage_clean.domain.use_cases

import kotlinx.coroutines.runBlocking
import yin_kio.garbage_clean.domain.entities.GarbageSelector
import yin_kio.garbage_clean.domain.gateways.Files
import yin_kio.garbage_clean.domain.gateways.Permissions
import yin_kio.garbage_clean.domain.gateways.StorageInfo
import yin_kio.garbage_clean.domain.services.garbage_files.GarbageType
import yin_kio.garbage_clean.domain.ui_out.Checkable
import yin_kio.garbage_clean.domain.ui_out.UiOuter
import java.io.File

class GarbageFilesUseCasesImpl(
    private val uiOuter: UiOuter,
    private val garbageSelector: GarbageSelector,
    private val permissions: Permissions,
    private val updateUseCase: UpdateUseCase,
    private val storageInfo: StorageInfo,
    private val files: Files
) : GarbageFilesUseCases {

    override fun closePermissionDialog(){
        uiOuter.closePermissionDialog()
    }

    override fun requestPermission(){
        uiOuter.requestPermission()
    }

    override fun switchItemSelection(group: GarbageType, file: File, itemCheckable: Checkable){
        garbageSelector.switchFileSelection(group, file)
        itemCheckable.setChecked(garbageSelector.isItemSelected(group, file))
        uiOuter.updateGroup(group)
    }

    override fun switchGroupSelection(group: GarbageType, groupCheckable: Checkable){
        garbageSelector.switchGroupSelected(group)
        groupCheckable.setChecked(garbageSelector.isGroupSelected(group))
        uiOuter.updateGroup(group)

    }

    override fun scan(){
        if (permissions.hasPermission){
            updateUseCase.update()
        } else {
            uiOuter.showPermissionDialog()
        }
    }

    override fun start(){

        if (permissions.hasPermission){
            updateUseCase.update()
            return
        }

        uiOuter.showPermissionRequired()

    }

    override fun clean() = runBlocking{
        storageInfo.saveStartVolume()
        uiOuter.showCleanProgress(listOf())
        files.deleteFiles(garbageSelector.getSelected())
        storageInfo.saveEndVolume()
        storageInfo.calculateEndVolume()
    }

    override fun closeInter(){
        uiOuter.showResult(storageInfo.freedVolume)
    }

}