package yin_kio.garbage_clean.domain.out

import yin_kio.garbage_clean.domain.entities.GarbageType

data class DeleteFormOutItem(
    val garbageType: GarbageType,
    val size: Long,
    val isSelected: Boolean
)