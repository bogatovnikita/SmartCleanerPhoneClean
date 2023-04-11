package yin_kio.garbage_clean.domain.ui_out

import yin_kio.garbage_clean.domain.services.garbage_files.GarbageType
import yin_kio.garbage_clean.domain.use_cases.UpdateState
import java.io.File

interface UiOuter {

    fun closePermissionDialog()
    fun showCleanProgress(files: List<File>)
    fun removeCleanProgressItem()

    fun showResult(result: Long)
    fun showPermissionDialog()
    fun requestPermission()

    fun updateGroup(group: GarbageType, hasSelected: Boolean)
    fun updateChildrenAndGroup(group: GarbageType, hasSelected: Boolean)
    fun outGarbage(garbage: List<Garbage>, wasClean: Boolean)
    fun showUpdateProgress(wasClean: Boolean)

    fun showPermissionRequired()
    fun hidePermissionRequired()

    fun updageLanguage(updateState: UpdateState, garbage: List<Garbage>, wasClean: Boolean)


}