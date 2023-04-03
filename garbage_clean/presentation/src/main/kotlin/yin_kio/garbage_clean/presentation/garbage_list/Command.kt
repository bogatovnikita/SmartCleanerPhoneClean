package yin_kio.garbage_clean.presentation.garbage_list

sealed interface Command{

    object ShowPermissionDialog : Command
    object ClosePermissionDialog : Command
    object RequestPermission: Command

}