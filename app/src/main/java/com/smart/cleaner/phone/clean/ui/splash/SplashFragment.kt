package com.smart.cleaner.phone.clean.ui.splash

import android.app.Dialog
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Shader
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.smart.cleaner.phone.clean.R
import com.smart.cleaner.phone.clean.databinding.FragmentSplashBinding
import kotlinx.coroutines.delay

class SplashFragment : DialogFragment(R.layout.fragment_splash) {

    private val binding: FragmentSplashBinding by viewBinding()

    private var isInterLoaded = false

    private val adsListener = {
        isInterLoaded = true
//        showInter(onClosed = {  //TODO реклама
            findNavController().navigate(R.id.action_to_boostFragment)
//        })
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        setStyle(STYLE_NO_FRAME, R.style.Dialog)
        preloadAndShowInter()
        return super.onCreateDialog(savedInstanceState)
    }

    private fun setGradientColor() {
        val shader = LinearGradient(
            0f, 0f, 0f, binding.appName.textSize,
            intArrayOf(Color.parseColor("#7a8ad5"), Color.parseColor("#BAC4F1")),
            null, Shader.TileMode.CLAMP
        )
        binding.appName.paint.shader = shader
    }

    override fun onResume() {
        super.onResume()
        render()
//        addAdsLoadedListener(adsListener) //TODO реклама
    }

    override fun onPause() {
        super.onPause()
//        removeAdsLoadedListener(adsListener) //TODO реклама
    }

    private fun render() {
        setGradientColor()
        dialog.apply { isCancelable = false }
    }

    private fun preloadAndShowInter() {
//        preloadAd()  //TODO реклама
        lifecycleScope.launchWhenResumed {
            delay(8000L)
            if (!isInterLoaded) {
                findNavController().navigate(R.id.action_to_boostFragment)
            }
        }
    }

}