package com.bogatovnikita.language_dialog.utils

import android.content.Context
import com.bogatovnikita.language_dialog.R
import java.util.*

class LocaleProvider(private val context: Context) {

    fun getCurrentLocaleModel(): LocaleModel {
        return LocaleModel.getLocale(PreferencesProvider(context).getLocale())
    }

    fun saveNewLocale(localeModel: LocaleModel) {
        val map = mutableMapOf<String, Any>()
        map["locale"] = localeModel.country
        PreferencesProvider(context).saveNewLocale(localeModel.country)
    }

    enum class LocaleModel(val country: String, val language: String, val image: Int) {
        DEFAULT("d_f", "en", R.drawable.ic_flag_not_selected),
        ENGLISH("gb", "en", R.drawable.ic_flag_uk),
        USA("us", "en", R.drawable.ic_flag_us),
        AUSTRALIA("au", "en", R.drawable.ic_flag_au),
        CANADA("ca", "en", R.drawable.ic_flag_ca),
        RUSSIA("ru", "ru", R.drawable.ic_flag_ru),
        GERMANY("de", "de", R.drawable.ic_flag_de),
        AUSTRIA("at", "de", R.drawable.ic_flag_at),
        FRANCE("fr", "fr", R.drawable.ic_flag_fr),
        SPAIN("es", "es", R.drawable.ic_flag_es),
        ITALY("it", "it", R.drawable.ic_flag_it),
        JAPAN("ja-jp", "ja", R.drawable.ic_flag_jp);

        companion object {
            fun getLocale(code: String): LocaleModel {
                if (code.contains("_")) {
                    var result: LocaleModel? = null
                    var languageCode = Locale.getDefault().language
                    try {
                        languageCode = code.split("_")[0].lowercase()
                        val countryCode = code.split("_")[1].lowercase()
                        result = values().firstOrNull {
                            it.country.contains(countryCode) && it.language.contains(languageCode)
                        }
                    } catch (e: Exception) {
                    }
                    if (result == null) {
                        result = values().firstOrNull { it.language.contains(languageCode) }
                    }
                    return result ?: DEFAULT
                } else {
                    return values().firstOrNull { it.country.contains(code) } ?: DEFAULT
                }
            }

            fun getValues() = values().filter { it != DEFAULT }
        }
    }
}