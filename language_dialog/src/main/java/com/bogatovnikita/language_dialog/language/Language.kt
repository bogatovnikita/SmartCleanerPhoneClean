package com.bogatovnikita.language_dialog.language

import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import com.bogatovnikita.language_dialog.utils.LocaleProvider
import com.bogatovnikita.language_dialog.utils.PreferencesProvider
import java.util.*

class Language(private val context: Context) {
    private val preferencesProvider = PreferencesProvider(context)
    private val localeProvider = LocaleProvider(preferencesProvider)

    fun changeLanguage(): Context {
        return setApplicationLanguage(localeProvider.getCurrentLocaleModel().language)
    }

    fun checkLanguage() = localeProvider.getCurrentLocaleModel().name == "DEFAULT"

    private fun setApplicationLanguage(localeCode: String): Context {
        val res: Resources = context.resources
        val conf: Configuration = res.configuration
        conf.setLocale(Locale(localeCode))
        return context.createConfigurationContext(conf)
    }
}