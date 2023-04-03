package yin_kio.garbage_clean.presentation.garbage_list.adapter.models

import yin_kio.garbage_clean.domain.services.garbage_files.GarbageType
import java.io.File

data class GarbageGroup(
    val type: GarbageType = GarbageType.Apk,
    val name: String = "",
    val files: List<File> = listOf(),
    val alpha: Float = 0.5f
)