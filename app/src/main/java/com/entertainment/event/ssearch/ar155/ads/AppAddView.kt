package com.entertainment.event.ssearch.ar155.ads

import android.content.Context
import android.util.AttributeSet
import com.ads.library.AdsManager
import com.ads.library.SubscriptionProvider
import com.google.android.gms.AdView

class Banner : AdView {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )


    init {
        init()
    }

    private fun init(){
        if (!SubscriptionProvider.getInstance(context).checkHasSubscription()) AdsManager.initBanner(this)
    }

}