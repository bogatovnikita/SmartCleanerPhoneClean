package com.example.ads

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.fragment.app.Fragment
import com.ads.library.AdsDelegate
import com.ads.library.AdsManager
import com.ads.library.SubscriptionProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


fun Fragment.preloadAd(){
    requireActivity().preloadAd()
}



fun Context.preloadAd(){
    if (!AdsManager.checkAdsLoaded()){
        Log.i("AdsWrapper", "preloadAds")
        CoroutineScope(Dispatchers.Main).launch { AdsManager.preloadAd(this@preloadAd, BuildConfig.ADMOB_INTERSTITIAL) }
    } else {
        Log.i("AdsWrapper", "Ads has already loaded")
    }
}

fun addAdsLoadedListener(loadedListener: () -> Unit) = AdsManager.addAdsLoadedListener(loadedListener)

fun removeAdsLoadedListener(loadedListener: () -> Unit) {
    AdsManager.removeAdsLoadedListener(loadedListener)
}


fun Activity.showInter(
    onClosed: () -> Unit = {},
    onOpened: () -> Unit = {},
){

    Log.i("AdsWrapper", "showInter")
    AdsManager.showInterstitial(this, object : AdsDelegate{
        override fun adsClosed() {
            onClosed()
        }

        override fun adsOpened() {
            onOpened()
        }
    })
}

fun Fragment.showInter(
    onClosed: () -> Unit = {}
){
    requireActivity().showInter(
        onClosed = onClosed
    )
}

fun Fragment.showInter(
    onClosed: () -> Unit = {},
    onOpened: () -> Unit = {},
){
    requireActivity().showInter(onClosed, onOpened)
}

fun Activity.initAds(){
    SubscriptionProvider.getInstance(this).init(
       this
    )
    AdsManager.init(this, BuildConfig.DEBUG)
}

fun Context.hasSubscription() = SubscriptionProvider.getInstance(this).checkHasSubscription()

fun Activity.emulateSubscription() = SubscriptionProvider.getInstance(this).emulateSubscription()