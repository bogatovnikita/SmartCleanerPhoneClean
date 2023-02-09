package com.softcleean.fastcleaner.ads

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import android.util.Log
import com.ads.library.types.AdmobProvider
import com.android.installreferrer.api.InstallReferrerClient
import com.android.installreferrer.api.InstallReferrerStateListener
import com.google.android.gms.ads.AdValue
import com.softcleean.fastcleaner.BuildConfig
import com.yandex.metrica.YandexMetrica
import java.net.URL
import java.util.concurrent.TimeUnit

object PostBackProvider {

    private const val CLICK_ID = "click_id"
    private const val INSTALL_DATE = "install_date"
    private const val INSTALL_EVENT_SENT = "install_event_sent"
    private const val PREFERENCES_NAME = "postbacks"
    private const val POSTBACK_LINK = "144.76.113.136"

    private lateinit var preferences: SharedPreferences

    fun init(activity: Activity) {
        preferences = activity.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
        if (preferences.getLong(INSTALL_DATE, 0L) == 0L) {
            preferences.edit().putLong(INSTALL_DATE, System.currentTimeMillis()).apply()
        }
        if (!preferences.getBoolean(INSTALL_EVENT_SENT, false)) {
            getInstallReferrer(activity)
        }

        initListeners()
    }

    private fun getInstallReferrer(activity: Activity) {
        val referrerClient = InstallReferrerClient.newBuilder(activity).build()
        referrerClient.startConnection(object : InstallReferrerStateListener {
            override fun onInstallReferrerSetupFinished(responseCode: Int) {
                when (responseCode) {
                    InstallReferrerClient.InstallReferrerResponse.OK -> {
                        val params = if (BuildConfig.DEBUG) {
                            "utm_medium=olejajew&utm_source=organic"
                        } else {
                            referrerClient.installReferrer.installReferrer
                        }
                        val parts =
                            params.split("&").filter { it.contains("utm_medium") }.firstOrNull()
                                ?.split("=")
                        val utmMedium = when (parts?.size) {
                            2 -> parts[1]
                            else -> "null"
                        }
                        YandexMetrica.reportEvent(
                            "install_referrer_params",
                            mapOf("utm_medium" to utmMedium)
                        )
                        preferences.edit().putString(CLICK_ID, utmMedium).apply()
                        installEvent()
                    }
                    InstallReferrerClient.InstallReferrerResponse.FEATURE_NOT_SUPPORTED -> {
                        YandexMetrica.reportEvent(
                            "install_referrer_error",
                            mapOf("cause" to "not_supported")
                        )
                    }
                    InstallReferrerClient.InstallReferrerResponse.SERVICE_UNAVAILABLE -> {
                        YandexMetrica.reportEvent(
                            "install_referrer_error",
                            mapOf("cause" to "service_unavailable")
                        )
                    }
                }
            }

            override fun onInstallReferrerServiceDisconnected() {
            }
        })
    }

    private fun installEvent() {
        if (!preferences.getBoolean(INSTALL_EVENT_SENT, false)) {
            Thread {
                try {
                    val result = URL(getUrl(Events.install)).readText()
                    preferences.edit().putBoolean(INSTALL_EVENT_SENT, true).apply()
                } catch (e: Exception) {
                    e.printStackTrace()
                    YandexMetrica.reportEvent(
                        "Install event error",
                        mapOf("error" to e.localizedMessage)
                    )
                }
            }.start()
        }
    }

    private fun getClickId() = preferences.getString(CLICK_ID, "")!!

    private fun getUrl(event: Events, amount: Double? = null): String {
        val builder = Uri.Builder()
            .scheme("http")
            .authority(POSTBACK_LINK)
            .appendPath("chbcl6k.php")
            .appendQueryParameter("cnv_id", getClickId())
            .appendQueryParameter("cnv_status", event.name)
        if(amount != null && event.eventName != null){
            builder.appendQueryParameter(event.eventName, amount.toString())
        }
        if (amount != null && event.needPayout) {
            builder.appendQueryParameter("payout", amount.toString())
        }
        val link = builder.build().toString()
        Log.e("!!!", link)
        return link
    }

    private fun revenueEvent(adValue: AdValue) {
        val amount = (adValue.valueMicros.toDouble() / 1000000)
        val days = Events.getDays(getDaysAfterInstall())
        Thread {
            days.forEach {
                try {
                    val result = URL(getUrl(it, amount)).readText()
                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                    YandexMetrica.reportEvent(
                        "Send revenue error",
                        mapOf("error" to e.localizedMessage)
                    )
                }
            }
        }.start()
    }

    private fun getDaysAfterInstall(): Long {
        val installDate = preferences.getLong(INSTALL_DATE, 0)
        return (System.currentTimeMillis() - installDate) / TimeUnit.DAYS.toMillis(1)
    }

    private fun initListeners() {
        AdmobProvider.setInterstitialPaidEventListener {
            revenueEvent(it)
        }
        AdmobProvider.setAppOpenPaidEventListener {
            revenueEvent(it)
        }
        AdmobProvider.setBannerPaidEventListener {
            revenueEvent(it)
        }
    }

    enum class Events(val eventName: String?, val needPayout: Boolean = false) {
        install(null),
        rev1("event_1", true),
        rev3("event_2"),
        rev7("event_3"),
        rev30("event_4"),
        revall("event_5");

        companion object {

            fun getDays(dayAfter: Long): List<Events> {
                val days = mutableListOf<Events>()
                days.add(revall)
                if (dayAfter < 30) {
                    days.add(rev30)
                }
                if (dayAfter < 7) {
                    days.add(rev7)
                }
                if (dayAfter < 3) {
                    days.add(rev3)
                }
                if (dayAfter < 1) {
                    days.add(rev1)
                }
                return days
            }

        }
    }

}