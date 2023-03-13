package com.example.permissions

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import java.lang.IllegalArgumentException


fun Fragment.runtimePermissionsLauncher(
    onResult: (Boolean) -> Unit = {}) : LazyPermissionLauncher {
    return LazyPermissionLauncher(this, onResult)
}

class LazyPermissionLauncher(
    private val fragment: Fragment,
    private val onResult: (Boolean) -> Unit
): Lazy<ActivityResultLauncher<Array<String>>>{

    private var launcher: ActivityResultLauncher<Array<String>>? = null

    init {
        fragment.lifecycle.addObserver(object : LifecycleEventObserver{
            override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                if (event == Lifecycle.Event.ON_CREATE){
                    launcher = fragment.registerForActivityResult(
                        ActivityResultContracts.RequestMultiplePermissions()){
                        onResult(!it.values.contains(false))
                    }
                } else if (event == Lifecycle.Event.ON_DESTROY){
                    launcher?.unregister()
                }
            }
        })
    }

    override val value: ActivityResultLauncher<Array<String>>
        get(){
            return launcher!!
        }

    override fun isInitialized(): Boolean {
        return launcher != null
    }
}


fun Fragment.requestRuntimePermissions(
    permissions: Array<String>,
    onResult: (Boolean) -> Unit
){
    val launcher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()){
        onResult(!it.values.contains(false))
    }

    launcher.launch(permissions)

    lifecycle.addObserver(object : LifecycleEventObserver{
        override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
            if (event == Lifecycle.Event.ON_STOP){
                launcher.unregister()
            }
        }
    })
}

fun Fragment.hasRuntime(vararg permission: String) : Boolean{
    return requireContext().hasRuntime(*permission)
}

fun Context.hasRuntime(vararg permission: String) : Boolean{
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) return true
    if (permission.isEmpty()) throw IllegalArgumentException("There is no permissions")
    val context = this
    permission.forEach {
        if (context.checkSelfPermission(it) != PackageManager.PERMISSION_GRANTED){
            return false
        }
    }
    return true
}