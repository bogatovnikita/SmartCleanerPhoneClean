package com.example.permissions

import android.Manifest
import android.content.Context
import android.os.Build
import androidx.fragment.app.Fragment

fun Fragment.hasStoragePermissions() : Boolean{
    return requireContext().hasStoragePermissions()
}

fun Context.hasStoragePermissions() : Boolean {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
        hasSpecial(Manifest.permission.MANAGE_EXTERNAL_STORAGE)
    } else {
        hasRuntime(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
    }
}