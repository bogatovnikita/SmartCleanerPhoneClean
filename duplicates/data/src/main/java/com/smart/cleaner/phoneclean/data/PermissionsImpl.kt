package com.smart.cleaner.phoneclean.data

import android.content.Context
import com.example.permissions.hasStoragePermissions
import com.smart.cleaner.phoneclean.domain.gateways.Permissions

class PermissionsImpl(
    private val context: Context
) : Permissions {

    override val hasStoragePermissions: Boolean
        get() = context.hasStoragePermissions()

}