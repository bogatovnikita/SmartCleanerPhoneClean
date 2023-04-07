package yin_kio.garbage_clean.presentation.garbage_list

import yin_kio.garbage_clean.domain.services.garbage_files.GarbageType

sealed interface Command{

    object ShowPermissionDialog : Command
    object ClosePermissionDialog : Command
    object RequestPermission: Command
    object ShowCleanProgress: Command
    object ShowResult : Command
    data class UpdateGroup(val garbageType: GarbageType): Command
    data class UpdateChildrenAndGroup(val garbageType: GarbageType) : Command

}