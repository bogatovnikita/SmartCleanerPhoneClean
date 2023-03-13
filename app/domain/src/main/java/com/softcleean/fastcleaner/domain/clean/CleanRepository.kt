package com.softcleean.fastcleaner.domain.clean

interface CleanRepository {

    fun getGarbageFilesCount(): IntArray

    fun getGarbageSizeArray(): IntArray

    fun getFolders(): Array<String>

    fun cleanGarbage()

    fun getTotalGarbageSize(): Int

    fun isGarbageCleared(): Boolean

    fun getTotalSizeMemory(): Long

    fun getUsedSizeMemory(): Long

    fun getGarbageNameArray(id: Int): Array<String>

}