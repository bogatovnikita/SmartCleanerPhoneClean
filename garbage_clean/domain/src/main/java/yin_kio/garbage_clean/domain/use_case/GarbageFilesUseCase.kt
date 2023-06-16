package yin_kio.garbage_clean.domain.use_case

import yin_kio.garbage_clean.domain.services.garbage_files.GarbageType
import yin_kio.garbage_clean.domain.ui_out.Checkable
import java.io.File

interface GarbageFilesUseCase {
    fun closePermissionDialog()
    fun requestPermission()
    fun switchItemSelection(group: GarbageType, file: File, itemCheckable: Checkable)
    fun switchGroupSelection(group: GarbageType, groupCheckable: Checkable)
    fun updateItemSelection(group: GarbageType, file: File, itemCheckable: Checkable)
    fun updateGroupSelection(group: GarbageType, groupCheckable: Checkable)

    fun closeInter()

    fun start()
    fun updateLanguage()
}