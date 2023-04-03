package yin_kio.garbage_clean.presentation.garbage_list

import yin_kio.garbage_clean.presentation.garbage_list.adapter.models.GarbageGroup

data class ScreenState(
    val size: String = "",
    val buttonText: String = "",
    val garbage: List<GarbageGroup> = listOf()
)