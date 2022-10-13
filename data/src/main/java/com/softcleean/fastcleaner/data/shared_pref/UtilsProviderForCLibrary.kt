package com.softcleean.fastcleaner.data.shared_pref

import android.app.Application
import android.content.ContentResolver
import android.content.Context
import android.content.SharedPreferences

object UtilsProviderForCLibrary {

    private lateinit var app: Application

    fun initUtilsProviderForCLibrary(application: Application) {
        app = application
    }

    fun getSharedPreferencesProvider(): SharedPreferences =
        app.getSharedPreferences("APP_PREFERENCES", Context.MODE_PRIVATE)

    fun getContentResolver(): ContentResolver = app.contentResolver
}