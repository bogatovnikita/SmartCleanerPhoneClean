package com.bogatovnikita.language_dialog.language

import android.app.Application
import com.bogatovnikita.language_dialog.utils.LocaleProvider
import java.util.*
import javax.inject.Inject

class Language @Inject constructor(
    private val context: Application,
    private val localeProvider: LocaleProvider
) {

    fun changeLanguage() {
        setApplicationLanguage(localeProvider.getCurrentLocaleModel().language)
    }

    private fun setApplicationLanguage(localeCode: String) {
        val locale = Locale.forLanguageTag(localeCode)
        val activityRes = context.resources
        val activityConf = activityRes.configuration
        activityConf.setLocale(locale)
        activityRes.updateConfiguration(activityConf, activityRes.displayMetrics)

        val applicationRes = context.resources
        val applicationConf = applicationRes.configuration
        applicationConf.setLocale(locale)
        applicationRes.updateConfiguration(
            applicationConf,
            applicationRes.displayMetrics
        )
    }
}