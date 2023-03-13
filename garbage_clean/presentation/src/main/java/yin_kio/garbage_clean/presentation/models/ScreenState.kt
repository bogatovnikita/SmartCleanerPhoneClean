package yin_kio.garbage_clean.presentation.models

import yin_kio.garbage_clean.domain.entities.GarbageType
import yin_kio.garbage_clean.domain.out.DeleteProgressState

data class ScreenState(
    val freed: String = "",
    val isClosed: Boolean = false,
    val deleteProgressState: DeleteProgressState = DeleteProgressState.Wait,
    val hasPermission: Boolean = true,
    val isInProgress: Boolean = true,
    val buttonState: ButtonState = ButtonState(),
    val fileSystemInfo: UiFileSystemInfo = UiFileSystemInfo(),
    val deleteFormItems: List<UiDeleteFromItem> = listOf(),
    val isAllSelected: Boolean = false,
    val canFreeVolume: String = "",
    val buttonText: String = "",
    val buttonBgRes: Int = general.R.drawable.bg_main_button_enabled,
    val deleteRequest: List<GarbageType> = emptyList()
)
