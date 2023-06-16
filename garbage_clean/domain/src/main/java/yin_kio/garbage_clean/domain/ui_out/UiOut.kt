package yin_kio.garbage_clean.domain.ui_out

sealed interface UiOut{

    object Init: UiOut
    object StartWithoutPermission : UiOut
    data class UpdateProgress(val isCleaned: Boolean) : UiOut
    data class Updated(val isCleaned: Boolean, val garbageOuts: List<Garbage>) : UiOut

}