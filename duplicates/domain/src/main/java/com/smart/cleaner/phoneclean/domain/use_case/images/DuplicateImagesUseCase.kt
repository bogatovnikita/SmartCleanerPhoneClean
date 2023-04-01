package com.smart.cleaner.phoneclean.domain.use_case.images

import com.smart.cleaner.phoneclean.domain.models.ImageInfo

interface DuplicateImagesUseCase {

    suspend fun getImageDuplicates(): List<List<ImageInfo>>

    suspend fun deleteDuplicates(duplicates: List<ImageInfo>)

    fun hasStoragePermissions(): Boolean

    fun saveDuplicatesDeleteTime()

}