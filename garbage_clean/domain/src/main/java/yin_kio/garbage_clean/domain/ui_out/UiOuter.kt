package yin_kio.garbage_clean.domain.ui_out

import yin_kio.garbage_clean.domain.services.garbage_files.GarbageType
import java.io.File

interface UiOuter {

    fun closePermissionDialog()
    fun requestPermission()
    fun updateGroup(group: GarbageType, hasSelected: Boolean)
    fun updateChildrenAndGroup(group: GarbageType, hasSelected: Boolean)
    fun showPermissionDialog()
    fun outGarbage(garbage: List<Garbage>)
    fun showUpdateProgress()
    fun showPermissionRequired()
    fun hidePermissionRequired()
    fun showCleanProgress(files: List<File>)
    fun showResult(result: Long)
    fun removeCleanProgressItem()

}