package com.bogatovnikita.language_dialog.utils

import android.content.Context
import android.content.res.Resources
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class PreferencesProvider @Inject constructor(@ApplicationContext context: Context) {

    private val preferences =
        context.getSharedPreferences(LOCALE_NAME_PREFS, Context.MODE_PRIVATE)

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
        private const val LOCALE_NAME_PREFS = "LOCALE_NAME_PREFS"
        private const val LOCALE = "LOCALE"
    }
}