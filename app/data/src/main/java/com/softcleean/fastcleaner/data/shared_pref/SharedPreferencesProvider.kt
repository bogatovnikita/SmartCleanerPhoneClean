package com.softcleean.fastcleaner.data.shared_pref

import android.app.Application
import android.content.Context
import javax.inject.Inject

class SharedPreferencesProvider @Inject constructor(private val application: Application) {

    private val sharedPreferences =
        application.getSharedPreferences(APPLICATION_NAME, Context.MODE_PRIVATE)

    fun saveBoost() {
        sharedPreferences.edit().putLong(SAVE_BOOST_TIME, System.currentTimeMillis()).apply()
    }

    fun getBoost(): Boolean {
        val currentTime = System.currentTimeMillis()
        val saveTime = sharedPreferences.getLong(SAVE_BOOST_TIME, 0L)
        return saveTime + FIFTEEN_MINUTES > currentTime
    }

    companion object {
        private const val APPLICATION_NAME = "APPLICATION_NAME"
        private const val SAVE_BOOST_TIME = "SAVE_BOOST_TIME"
        private const val FIFTEEN_MINUTES = 900_000L
    }
}