package bogatovnikita.premium.presentation.ui

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import bogatovnikita.premium.presentation.base.BaseFragment
import bogatovnikita.premium.presentation.databinding.FragmentPremiumScreenBinding
import com.example.ads.setOnSetupFinished
import com.example.ads.setSubscriptionListener
import com.example.ads.startSubscription

class PremiumScreenFragment :
    BaseFragment<FragmentPremiumScreenBinding>(FragmentPremiumScreenBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setOnSetupFinished()
        initClickListener()
        premiumActivated()
    }

    private fun initClickListener() {
        binding.btnStart.setOnClickListener {
            startSubscription()
        }
        binding.privacyPolice.setOnClickListener {
            // TODO добавить переход
        }

        binding.restorePurchases.setOnClickListener {
            // TODO добавить переход
        }

        binding.termsOfUse.setOnClickListener {
            // TODO добавить переход
        }

    }

    private fun premiumActivated() {
        setSubscriptionListener { closePremiumScreen() }
    }

    private fun closePremiumScreen() {
        findNavController().popBackStack()
    }
}