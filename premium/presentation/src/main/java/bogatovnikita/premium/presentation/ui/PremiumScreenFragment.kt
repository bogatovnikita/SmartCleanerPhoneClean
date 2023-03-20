package bogatovnikita.premium.presentation.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import bogatovnikita.premium.presentation.base.BaseFragment
import bogatovnikita.premium.presentation.databinding.FragmentPremiumScreenBinding
import com.bogatovnikita.language_dialog.ui.LocalDialog
import com.bogatovnikita.language_dialog.utils.LocaleProvider
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
        initLocale()
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

        binding.languageSelection.setOnClickListener {
            openLocalDialog()
        }
    }

    private fun premiumActivated() {
        setSubscriptionListener { closePremiumScreen() }
    }

    private fun closePremiumScreen() {
        findNavController().popBackStack()
    }

    private fun initLocale() {
        binding.languageSelection.setImageResource(LocaleProvider(requireContext()).getCurrentLocaleModel().image)
    }

    private fun openLocalDialog() {
        val dialog = LocalDialog(requireContext()) {
            val intent: Intent = requireActivity().intent
            requireActivity().finish()
            startActivity(intent)
        }
        dialog.show()
    }
}