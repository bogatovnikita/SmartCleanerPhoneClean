package com.smart.cleaner.phoneclean.ads

import android.app.Activity
import androidx.fragment.app.Fragment
import com.ads.library.AdsDelegate
import com.ads.library.AdsManager
import com.ads.library.SubscriptionProvider
import com.smart.cleaner.phoneclean.BuildConfig

fun Fragment.showInterstitial(
    onClosed: () -> Unit = {},
    onLoaded: () -> Unit = {},
    onOpened: () -> Unit = {},
    needed: Boolean = false
) {
    requireActivity().showInterstitial(
        onClosed, onLoaded, onOpened, needed
    )
}

inline fun Activity.showInterstitial(
    crossinline onClosed: () -> Unit = {},
    crossinline onLoaded: () -> Unit = {},
    crossinline onOpened: () -> Unit = {},
    needed: Boolean = true
) {

    AdsManager.showInterstitial(this, object : AdsDelegate {
        override fun adsClosed() {
            onClosed()
        }

        override fun adsLoaded() {
            onLoaded()
        }

        override fun adsOpened() {
            onOpened()
        }
    }, needed)

}

fun Fragment.addAdsLoadedListener(listener: () -> Unit) = AdsManager.addAdsLoadedListener(listener)

fun Fragment.removeAdsLoadedListener(listener: () -> Unit) = AdsManager.removeAdsLoadedListener(listener)

fun Fragment.preloadInterstitial(key: String) {
    if (!AdsManager.checkAdsLoaded()) {
        requireActivity().preloadInterstitial(key)
    }
}

fun Activity.preloadInterstitial(key: String) {
    AdsManager.preloadAd(this, key)
}

fun Activity.initAdsLib() {
    AdsManager.init(this, BuildConfig.DEBUG)
}

fun Activity.emulateSubscription() = SubscriptionProvider.getInstance(this).emulateSubscription()

