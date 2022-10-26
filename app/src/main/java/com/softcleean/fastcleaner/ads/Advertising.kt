//import android.app.Activity
//import androidx.fragment.app.Fragment
//import com.ads.library.AdsDelegate
//import com.ads.library.AdsManager
//import com.ads.library.SubscriptionProvider
//import com.softcleean.fastcleaner.BuildConfig
//
//fun Fragment.showInterstitial(
//    onClosed: () -> Unit = {},
//    onLoaded: () -> Unit = {},
//    onOpened: () -> Unit = {},
//    needed: Boolean = false
//) {
//    requireActivity().showInterstitial(
//        onClosed, onLoaded, onOpened, needed
//    )
//}
//
//inline fun Activity.showInterstitial(
//    crossinline onClosed: () -> Unit = {},
//    crossinline onLoaded: () -> Unit = {},
//    crossinline onOpened: () -> Unit = {},
//    needed: Boolean = true
//) {
//
//    AdsManager.showInterstitial(this, object : AdsDelegate {
//        override fun adsClosed() {
//            onClosed()
//        }
//
//        override fun adsLoaded() {
//            onLoaded()
//        }
//
//        override fun adsOpened() {
//            onOpened()
//        }
//    }, needed)
//
//}
//
//fun Fragment.preloadInterstitial(key: String) {
//    requireActivity().preloadInterstitial(key)
//}
//
//fun Activity.preloadInterstitial(key: String) {
//    AdsManager.preloadAd(this, key)
//}
//
//fun Activity.initAds() {
////    SubscriptionProvider.getInstance(this).init( TODO
////        this,
////        SUBSCRIPTION_WEEK_KEY,
////        SUBSCRIPTION_MONTH_KEY,
////        SUBSCRIPTION_YEAR_KEY
////    )
//    AdsManager.init(this, BuildConfig.DEBUG)
////    AdsManager.fetchAppOpenManager(this, BuildConfig.ADMOB_APP_OPEN, false) TODO
//}
//
//fun Activity.emulateSubscription() = SubscriptionProvider.getInstance(this).emulateSubscription()
