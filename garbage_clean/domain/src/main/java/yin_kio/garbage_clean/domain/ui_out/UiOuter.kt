package yin_kio.garbage_clean.domain.ui_out

import yin_kio.garbage_clean.domain.services.garbage_files.GarbageType

interface UiOuter {

    fun closePermissionDialog()
    fun requestPermission()
    fun updateGroup(group: GarbageType)
    fun showPermissionDialog()
    fun outGarbage(garbage: List<Garbage>)
    fun showUpdateProgress()
    fun showPermissionRequired()
    fun showCleanProgress(messages: List<String>)
    fun showResult(result: Long)

}