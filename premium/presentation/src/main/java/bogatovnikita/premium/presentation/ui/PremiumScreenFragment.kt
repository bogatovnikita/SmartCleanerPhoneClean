package bogatovnikita.premium.presentation.ui

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.DialogFragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import bogatovnikita.premium.presentation.R
import bogatovnikita.premium.presentation.databinding.FragmentPremiumScreenBinding
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.ads.setOnSetupFinished
import com.example.ads.setSubscriptionListener
import com.example.ads.startSubscription

class PremiumScreenFragment : DialogFragment(R.layout.fragment_premium_screen) {

    private val binding: FragmentPremiumScreenBinding by viewBinding()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        setStyle(STYLE_NO_FRAME, general.R.style.Dialog)
        return super.onCreateDialog(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setOnSetupFinished()
        initClickListener()
        premiumActivated()
    }

    private fun initClickListener() {
        binding.startTrial.setOnClickListener {
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