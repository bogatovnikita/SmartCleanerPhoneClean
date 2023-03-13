package com.example.permissions

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AppOpsManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.Settings
import androidx.annotation.RequiresApi

@SuppressLint("InlinedApi")
private val opByPermission = mapOf(
    Manifest.permission.PACKAGE_USAGE_STATS to AppOpsManager.OPSTR_GET_USAGE_STATS,
    Manifest.permission.WRITE_SETTINGS to AppOpsManager.OPSTR_WRITE_SETTINGS
)

@SuppressLint("NewApi")
fun Context.hasSpecial(permission: String): Boolean {
    val currentVersion = Build.VERSION.SDK_INT
    return when{
        currentVersion < Build.VERSION_CODES.M -> true
        permission == Manifest.permission.MANAGE_EXTERNAL_STORAGE
                && currentVersion >= Build.VERSION_CODES.R -> Environment.isExternalStorageManager()
        else -> byAppOpsManager(permission)
    }
}

private fun Context.byAppOpsManager(permission: String): Boolean {
    val mode = mode(this, opByPermission[permission]!!)
    return if (mode == AppOpsManager.MODE_DEFAULT) {
        checkCallingOrSelfPermission(permission) == PackageManager.PERMISSION_GRANTED
    } else {
        mode == AppOpsManager.MODE_ALLOWED
    }
}

private fun Context.mode(context: Context, opstr: String): Int {
    val appOps = getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        appOps.unsafeCheckOpNoThrow(
            opstr,
            android.os.Process.myUid(),
            context.packageName
        )
    } else {
        appOps.checkOpNoThrow(
            opstr,
            android.os.Process.myUid(),
            context.packageName
        )
    }
}



fun Activity.requestPackageUsageStats() {
    requestThroughSettings(Settings.ACTION_USAGE_ACCESS_SETTINGS)
}

@RequiresApi(Build.VERSION_CODES.M)
fun Activity.requestWritheSettings() {
    requestThroughSettings(Settings.ACTION_MANAGE_WRITE_SETTINGS)
}

@RequiresApi(Build.VERSION_CODES.R)
fun Activity.requestManageExternalStorage() {
    requestThroughSettings(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
}

private fun Activity.requestThroughSettings(action: String) {
    try {
        val intent = Intent(action)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
        intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)
        val uri: Uri = Uri.fromParts("package", packageName, null)
        intent.data = uri
        intent.putExtra("packageName", packageName)
        startActivity(intent)
    } catch (e: Exception) {
        val intent = Intent(action)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
        intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)
        startActivity(intent)
    }

}


