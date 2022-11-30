package com.softcleean.fastcleaner.ui.splash

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.softcleean.fastcleaner.R
import com.softcleean.fastcleaner.databinding.FragmentSplashBinding
import kotlinx.coroutines.delay

class SplashFragment : Fragment(R.layout.fragment_splash) {

    private val binding: FragmentSplashBinding by viewBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding
        lifecycleScope.launchWhenResumed {
            delay(1000)
            findNavController().navigate(R.id.action_to_boostFragment)
        }
    }
}