package yin_kio.garbage_clean.presentation.garbage_list

import com.smart.cleaner.phoneclean.ui_core.adapters.models.GeneralOptimizingItem
import yin_kio.garbage_clean.presentation.garbage_list.adapter.models.GarbageGroup

data class ScreenState(
    val size: String = "",
    val buttonText: String = "",
    val garbage: List<GarbageGroup> = listOf(),
    val isShowPermissionRequired: Boolean = false,
    val buttonOpacity: Float = 1f,
    val cleanMessages: List<GeneralOptimizingItem> = listOf(),
    val freedSpace: String = "",
    val message: String = "",
    val messageColor: Int = 0x000000

)