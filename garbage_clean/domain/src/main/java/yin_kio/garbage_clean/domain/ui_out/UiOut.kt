package yin_kio.garbage_clean.domain.ui_out

sealed interface UiOut{

    object StartWithoutPermission : UiOut
    data class UpdateProgress(val isCleaned: Boolean) : UiOut

}