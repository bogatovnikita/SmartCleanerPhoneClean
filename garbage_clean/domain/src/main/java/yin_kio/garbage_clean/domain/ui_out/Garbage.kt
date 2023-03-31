package yin_kio.garbage_clean.domain.ui_out

import yin_kio.garbage_clean.domain.services.garbage_files.GarbageType
import java.io.File

data class Garbage(
    val type: GarbageType = GarbageType.Apk,
    val files: List<File> = listOf()
)