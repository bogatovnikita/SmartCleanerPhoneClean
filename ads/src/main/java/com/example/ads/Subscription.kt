package com.example.ads

import android.app.Activity
import android.content.Context
import androidx.fragment.app.Fragment
import com.ads.library.AdsManager
import com.ads.library.SubscriptionProvider

fun Activity.initSubscription() {
    SubscriptionProvider.getInstance(this).init(
        this, BuildConfig.SUBSCRIPTION_ID
    )
    AdsManager.init(this, BuildConfig.DEBUG)
}

fun Context.hasSubscription() = SubscriptionProvider.getInstance(this).checkHasSubscription()

fun Activity.emulateSubscription() =
    SubscriptionProvider.getInstance(this).emulateSubscription()

fun Fragment.setOnSetupFinished() {
    val subscriptionProvider = SubscriptionProvider.getInstance(requireActivity())
    subscriptionProvider.setOnSetupFinished {
        val sku = subscriptionProvider.getSku(BuildConfig.SUBSCRIPTION_ID)
        sku?.subscriptionOfferDetails?.get(0)?.pricingPhases?.pricingPhaseList?.get(0)?.formattedPrice
    }
}

fun Fragment.setSubscriptionListener(onClosed: () -> Unit = {}) = onClosed()

fun Fragment.startSubscription() = SubscriptionProvider.getInstance(requireActivity())
    .startSubscription(requireActivity(), BuildConfig.SUBSCRIPTION_ID)
