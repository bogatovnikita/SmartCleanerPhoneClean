package yin_kio.garbage_clean.data

import android.content.Context
import com.example.ads.preloadAd
import yin_kio.garbage_clean.domain.gateways.Ads

class OlejaAds(
    private val context: Context
) : Ads{

    override suspend fun preloadAd() {
        context.preloadAd()
    }
}