package yin_kio.garbage_clean.domain.ui_out

interface UiOuter {

    fun closePermissionDialog()
    fun requestPermission()
    fun updateGroup(index: Int)
    fun showPermissionDialog()
    fun outGarbage(garbage: List<Garbage>)

}