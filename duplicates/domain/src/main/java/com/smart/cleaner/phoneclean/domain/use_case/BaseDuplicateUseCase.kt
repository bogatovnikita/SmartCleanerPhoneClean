package com.smart.cleaner.phoneclean.domain.use_case

import com.smart.cleaner.phoneclean.domain.gateways.DuplicatesSettings
import com.smart.cleaner.phoneclean.domain.gateways.Files
import com.smart.cleaner.phoneclean.domain.gateways.Permissions
import com.smart.cleaner.phoneclean.domain.models.ImageInfo

open class BaseDuplicateUseCase(
    private val files: Files,
    private val permissions: Permissions,
    private val settings: DuplicatesSettings,
) {

    suspend fun deleteDuplicates(duplicates: List<ImageInfo>) =
        duplicates.forEach { image ->
            files.delete(image.path)
        }

    fun hasStoragePermissions(): Boolean = permissions.hasStoragePermissions

    fun saveDuplicatesDeleteTime() = settings.saveDuplicatesDeleteTime()

}