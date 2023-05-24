package yin_kio.garbage_clean.presentation.garbage_list

import com.smart.cleaner.phoneclean.ui_core.adapters.models.GeneralOptimizingItem
import yin_kio.garbage_clean.presentation.garbage_list.adapter.models.GarbageGroup

data class ScreenState(
    val sizeText: String = "",
    val buttonText: String = "",
    val garbageGroups: List<GarbageGroup> = listOf(),
    val isShowPermissionRequired: Boolean = false,
    val buttonOpacity: Float = 1f,
    val cleanMessages: List<GeneralOptimizingItem> = listOf(),
    val freedSpace: String = "",
    val message: String = "",
    val messageColor: Int = 0x000000,
    val sizeMessageColor: Int = 0x00000,
    val isExpandEnabled: Boolean = false

)