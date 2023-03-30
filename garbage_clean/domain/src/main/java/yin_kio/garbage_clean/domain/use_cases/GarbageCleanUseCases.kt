package yin_kio.garbage_clean.domain.use_cases

import yin_kio.garbage_clean.domain.garbage_files.GarbageType
import yin_kio.garbage_clean.domain.out.Navigator

interface GarbageCleanUseCases {
    fun switchSelectAll()
    fun switchSelection(garbageType: GarbageType)
    fun deleteIfCan(navigator: Navigator)
    fun update(navigator: Navigator)
    fun close(navigator: Navigator)
}