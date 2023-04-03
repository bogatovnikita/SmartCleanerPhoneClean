package yin_kio.garbage_clean.presentation

import yin_kio.garbage_clean.presentation.adapter.models.GarbageGroup

data class ScreenState(
    val size: String = "",
    val buttonText: String = "",
    val garbage: List<GarbageGroup> = listOf()
)