package yin_kio.garbage_clean.domain.use_cases

import yin_kio.garbage_clean.domain.entities.GarbageType

interface GarbageCleanUseCases {
    fun switchSelectAll()
    fun switchSelection(garbageType: GarbageType)
    fun deleteIfCan()
    fun update()
    fun close()
}