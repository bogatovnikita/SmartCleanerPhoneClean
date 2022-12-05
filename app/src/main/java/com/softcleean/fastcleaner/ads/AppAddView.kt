//package com.softcleean.fastcleaner.ads
//
//import android.content.Context
//import android.util.AttributeSet
//import com.ads.library.AdsManager
//import com.ads.library.SubscriptionProvider
//import com.google.android.gms.AdView
//
//class Banner @JvmOverloads constructor(
//    context: Context,
//    private val attr: AttributeSet? = null,
//    private val defStyleAttr: Int = 0
//) : AdView(context, attr, defStyleAttr) {
//
//    init {
//        init()
//    }
//
//    private fun init(){
//        if (!SubscriptionProvider.getInstance(context).checkHasSubscription()) AdsManager.initBanner(this)
//    }
//
//}