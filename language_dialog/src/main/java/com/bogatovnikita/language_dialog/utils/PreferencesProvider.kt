package com.bogatovnikita.language_dialog.utils

import android.content.Context
import android.content.res.Resources

class PreferencesProvider(context: Context) {

    private val preferences =
        context.getSharedPreferences(LOCALE_NAME, Context.MODE_PRIVATE)

    fun getLocale(): String {
        return preferences.getString(LOCALE, getCountryCode())!!
    }

    fun saveNewLocale(localeCode: String) {
        preferences.edit().putString(LOCALE, localeCode).apply()
    }

    private fun getCountryCode(): String {
        return Resources.getSystem().configuration.locale.toString()
    }

    companion object {
        private const val LOCALE_NAME = "LOCALE_NAME"
        private const val LOCALE = "LOCALE"
    }
}