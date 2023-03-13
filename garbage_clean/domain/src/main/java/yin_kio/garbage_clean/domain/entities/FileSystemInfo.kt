package yin_kio.garbage_clean.domain.entities

data class FileSystemInfo(
    val occupied: Long = 0,
    val available: Long = 0,
    val total: Long = 0
)