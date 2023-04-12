package com.smartcleaner.pro.clean.models

import android.graphics.drawable.Drawable

data class BackgroundApp(
    val name: String = "",
    val icon: Drawable? = null,
    val packageName: String = ""
)