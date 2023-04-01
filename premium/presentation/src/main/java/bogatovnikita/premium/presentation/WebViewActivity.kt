package bogatovnikita.premium.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity

class WebViewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_view)
        openWebView()
    }

    private fun openWebView() {
        val url = intent.getStringExtra(TYPE_PAGE) ?: PRIVACY_POLICY
        val webView = findViewById<WebView>(R.id.web_view)
        webView.webViewClient = WebViewClient()
        webView.loadUrl(url)

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
    }
}