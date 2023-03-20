package com.smart.cleaner.phoneclean.ui.boost

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bogatovnikita.language_dialog.ui.LocalDialog
import com.bogatovnikita.language_dialog.utils.LocaleProvider
import com.smart.cleaner.phoneclean.R
import com.smart.cleaner.phoneclean.databinding.FragmentBoostBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class BoostFragment : Fragment(R.layout.fragment_boost) {

    private val binding: FragmentBoostBinding by viewBinding()

    private val viewModel: BoostViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getParams()
        initScreenStateObserver()
        setBtnListeners()
        initLocale()
    }

    private fun initScreenStateObserver() {
        lifecycleScope.launch {
            viewModel.screenState.collect { state ->
                renderState(state)
            }
        }
    }

    private fun renderState(state: BoostScreenState) {
        with(state) {
            setEnableBtn(!isRamBoosted)
            with(binding) {
                circularProgressRamPercentDuplicate.progress = ramPercent.toFloat()
                tvRamPercentsDuplicate.text = getString(R.string.value_percents, ramPercent)
                tvUsedRam.text = getString(R.string.gb, usedRam)
                tvTotalRam.text = getString(R.string.gb_one_after_dot, totalRam)
                tvFreeRam.text = getString(R.string.gb, freeRam)
                tvRamBoosted.isVisible = isRamBoosted
                tvRamUnboosted.isVisible = !isRamBoosted
            }
        }
    }

    private fun setEnableBtn(enable: Boolean) {
        binding.btnBoostBattery.isClickable = enable
        binding.btnBoostBattery.isEnabled = enable
    }

    private fun setBtnListeners() {
        binding.btnBoostBattery.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_boostOptimizingFragment)
        }

        binding.btnChangeLanguage.setOnClickListener {
            openLocalDialog()
        }
    }

    private fun openLocalDialog() {
        val dialog = LocalDialog(requireContext()) {
            val intent: Intent = requireActivity().intent
            requireActivity().finish()
            startActivity(intent)
        }
        dialog.show()
    }

    private fun initLocale() {
        binding.btnChangeLanguage.setImageResource(LocaleProvider(requireContext()).getCurrentLocaleModel().image)
    }
}