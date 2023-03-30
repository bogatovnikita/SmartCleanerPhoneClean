package yin_kio.garbage_clean.presentation.models

import yin_kio.garbage_clean.domain.garbage_files.GarbageType

data class UiDeleteFromItem(
    val garbageType: GarbageType,
    val iconRes: Int,
    val name: String,
    val size: String,
    val isSelected: Boolean
)