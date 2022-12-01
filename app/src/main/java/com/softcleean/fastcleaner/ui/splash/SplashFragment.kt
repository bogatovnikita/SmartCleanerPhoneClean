package com.softcleean.fastcleaner.ui.splash

import android.app.Dialog
import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.softcleean.fastcleaner.R
import com.softcleean.fastcleaner.databinding.FragmentSplashBinding
import kotlinx.coroutines.delay

class SplashFragment : DialogFragment(R.layout.fragment_splash) {

    private val binding: FragmentSplashBinding by viewBinding()


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        setStyle(STYLE_NO_FRAME, R.style.Dialog)
        return super.onCreateDialog(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog.apply { isCancelable = false }
        binding
        lifecycleScope.launchWhenResumed {
            delay(1000)
            findNavController().navigate(R.id.action_to_boostFragment)
        }
    }
}