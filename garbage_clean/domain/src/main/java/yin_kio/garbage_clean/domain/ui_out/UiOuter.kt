package yin_kio.garbage_clean.domain.ui_out

import yin_kio.garbage_clean.domain.services.garbage_files.GarbageType
import java.io.File

interface UiOuter {

    fun closePermissionDialog()
    fun showCleanProgress(files: List<File>)

    fun showResult(result: Long)
    fun showPermissionDialog()
    fun requestPermission()

    fun updateGroup(group: GarbageType, hasSelected: Boolean)
    fun updateChildrenAndGroup(group: GarbageType, hasSelected: Boolean)



    fun showAttentionMessagesColors()


    fun out(uiOut: UiOut)
    fun changeLanguage(uiOut: UiOut)


}