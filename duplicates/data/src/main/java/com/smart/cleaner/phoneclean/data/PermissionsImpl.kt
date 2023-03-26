package com.smart.cleaner.phoneclean.data

import android.app.Application
import com.example.permissions.hasStoragePermissions
import com.smart.cleaner.phoneclean.domain.gateways.Permissions
import javax.inject.Inject

class PermissionsImpl @Inject constructor(
    private val context: Application
) : Permissions {

    override val hasStoragePermissions: Boolean
        get() = context.hasStoragePermissions()

}