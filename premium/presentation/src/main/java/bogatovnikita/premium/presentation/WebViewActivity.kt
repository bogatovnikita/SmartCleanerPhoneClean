package bogatovnikita.premium.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import bogatovnikita.premium.presentation.databinding.ActivityWebViewBinding
import by.kirich1409.viewbindingdelegate.viewBinding

class WebViewActivity : AppCompatActivity() {

    private val binding: ActivityWebViewBinding by viewBinding()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_view)
        openWebView()
        initClickListener()
    }

    private fun openWebView() {
        val url = intent.getStringExtra(TYPE_PAGE) ?: PRIVACY_POLICY
        val webView = findViewById<WebView>(R.id.web_view)
        webView.webViewClient = WebViewClient()
        webView.loadUrl(url)

    }

    private fun initClickListener() {
        binding.back.setOnClickListener {
            onBackPressed()
        }
    }

    companion object {
        fun getIntent(context: Context, pageType: String): Intent {
            val intent = Intent(context, WebViewActivity::class.java)
            intent.putExtra(TYPE_PAGE, pageType)
            return intent
        }

        const val TYPE_PAGE = "TYPE_PAGE"
        const val PRIVACY_POLICY = "file:///android_asset/Privacy_policy.html"
        const val TERMS_AND_CONDITIONS = "file:///android_asset/Terms_conditions.html"
        const val HOW_TO_CANCEL_SUBSCRIPTION_DE =
            "file:///android_asset/how_to_cancel_subscription_de.html"
        const val HOW_TO_CANCEL_SUBSCRIPTION_EN =
            "file:///android_asset/how_to_cancel_subscription_en.html"
        const val HOW_TO_CANCEL_SUBSCRIPTION_FR =
            "file:///android_asset/how_to_cancel_subscription_fr.html"
        const val HOW_TO_CANCEL_SUBSCRIPTION_IT =
            "file:///android_asset/how_to_cancel_subscription_it.html"
        const val HOW_TO_CANCEL_SUBSCRIPTION_JP =
            "file:///android_asset/how_to_cancel_subscription_jp.html"
        const val HOW_TO_CANCEL_SUBSCRIPTION_RU =
            "file:///android_asset/how_to_cancel_subscription_ru.html"
        const val HOW_TO_CANCEL_SUBSCRIPTION_SP =
            "file:///android_asset/how_to_cancel_subscription_sp.html"
    }
}