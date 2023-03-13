package com.softcleean.fastcleaner.domain.clean

import javax.inject.Inject

class CleanUseCase @Inject constructor(
    private val cleanRepository: CleanRepository
) {

    fun getGarbageFilesCount(): IntArray = cleanRepository.getGarbageFilesCount()

    fun getGarbageSizeArray(): IntArray = cleanRepository.getGarbageSizeArray()

    fun getFolders(): Array<String> = cleanRepository.getFolders()

    fun cleanGarbage() = cleanRepository.cleanGarbage()

    fun getTotalGarbageSize(): Int = cleanRepository.getTotalGarbageSize()

    fun isGarbageCleared(): Boolean = cleanRepository.isGarbageCleared()

    fun getGarbageNameArray(id: Int): Array<String> = cleanRepository.getGarbageNameArray(id)

    fun getTotalSizeMemory(): Long = cleanRepository.getTotalSizeMemory()

    fun getUsedSizeMemory(): Long = cleanRepository.getUsedSizeMemory()

}