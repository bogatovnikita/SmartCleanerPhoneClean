package com.smart.cleaner.phoneclean.domain.use_case

import com.smart.cleaner.phoneclean.domain.gateways.DuplicatesSettings
import com.smart.cleaner.phoneclean.domain.gateways.Files
import com.smart.cleaner.phoneclean.domain.gateways.Permissions

open class BaseDuplicateUseCase(
    private val files: Files,
    private val permissions: Permissions,
    private val settings: DuplicatesSettings,
) {

    suspend fun deleteDuplicates(duplicates: List<String>) =
        duplicates.forEach { path ->
            files.delete(path)
        }

    fun hasStoragePermissions(): Boolean = permissions.hasStoragePermissions

    fun saveDuplicatesDeleteTime() = settings.saveDuplicatesDeleteTime()

}