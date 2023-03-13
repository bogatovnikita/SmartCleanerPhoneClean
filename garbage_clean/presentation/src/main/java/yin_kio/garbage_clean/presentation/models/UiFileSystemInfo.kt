package yin_kio.garbage_clean.presentation.models

data class UiFileSystemInfo(
    val occupied: String = "",
    val available: String = "",
    val total: String = "",
    val occupiedPercents: Float = 0f
)