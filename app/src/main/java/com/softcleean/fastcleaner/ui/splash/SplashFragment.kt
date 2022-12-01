package com.softcleean.fastcleaner.ui.splash

import addAdsLoadedListener
import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.softcleean.fastcleaner.BuildConfig
import com.softcleean.fastcleaner.R
import com.softcleean.fastcleaner.databinding.FragmentSplashBinding
import kotlinx.coroutines.delay
import preloadInterstitial
import removeAdsLoadedListener
import showInterstitial

class SplashFragment : DialogFragment(R.layout.fragment_splash) {

    private val binding: FragmentSplashBinding by viewBinding()
    private var isInterLoaded = false

    private val adsListener = {
        isInterLoaded = true
        showInterstitial(onClosed = {
            findNavController().navigate(R.id.action_to_boostFragment)
        })
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        setStyle(STYLE_NO_FRAME, R.style.Dialog)
        preloadAndShowInter()
        return super.onCreateDialog(savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        render()
        addAdsLoadedListener(adsListener)
    }

    override fun onPause() {
        super.onPause()
        removeAdsLoadedListener(adsListener)
    }

    private fun render() {
        dialog.apply { isCancelable = false }
        binding.tvPrivacyPolicy.setOnClickListener { openPrivacyPolicy() }
    }

    private fun preloadAndShowInter() {
        preloadInterstitial(BuildConfig.ADMOB_INTERSTITIAL1)
        lifecycleScope.launchWhenResumed {
            delay(9000L)
            if (!isInterLoaded) {
                findNavController().navigate(R.id.action_to_boostFragment)
            }
        }
    }

    private fun openPrivacyPolicy() {
        startActivity(
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://sites.google.com/view/fastcleanersft/")
            )
        )
    }
}