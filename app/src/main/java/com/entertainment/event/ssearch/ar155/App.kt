package com.entertainment.event.ssearch.ar155

import android.app.Application
import com.entertainment.event.ssearch.data.shared_pref.UtilsProviderForCLibrary
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App: Application() {
    init {
        UtilsProviderForCLibrary.initUtilsProviderForCLibrary(this)
    }
}