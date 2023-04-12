package com.smartcleaner.pro.clean.ui.boost

import android.Manifest
import android.app.AppOpsManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Process
import android.provider.Settings
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.smartcleaner.pro.clean.R
import com.smartcleaner.pro.clean.databinding.FragmentBoostBinding
import com.smartcleaner.pro.clean.ui.dialogs.DialogRequestUsageState
import com.smartcleaner.pro.clean.ui.dialogs.DialogRequestUsageState.Companion.RESULT_USAGE_STATE
import com.smartcleaner.pro.clean.ui.dialogs.DialogRequestUsageState.Companion.RESULT_USAGE_STATE_KEY
import com.smartcleaner.pro.clean.ui.dialogs.DialogRequestUsageState.Companion.RESULT_USAGE_STATE_SUCCESS
import com.smartcleaner.pro.clean.ui.dialogs.DialogRequestUsageState.Companion.TAG_USAGE_STATE
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BoostFragment : Fragment(R.layout.fragment_boost) {

    private val binding: FragmentBoostBinding by viewBinding()

    private val viewModel: BoostViewModel by viewModels()

    private val startActivityForResultUsageState =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            viewModel.setUsageStatePermission(checkPackageUsageStatePermission())
        }

    override fun onResume() {
        super.onResume()
        viewModel.getParams()
        checkPermission()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setOnDialogCallback()
        initScreenStateObserver()
        setBtnListeners()
    }

    private fun setOnDialogCallback() {
        setFragmentResultListener(RESULT_USAGE_STATE) { _, bundle ->
            if ((bundle.getString(RESULT_USAGE_STATE_KEY) ?: "") == RESULT_USAGE_STATE_SUCCESS) {
                requestUsageStatePermission()
            }
        }
    }

    private fun requestUsageStatePermission() {
        startActivityForResultUsageState.launch(Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS))
    }

    private fun checkPermission() {
        if (checkPackageUsageStatePermission()) {
            viewModel.setUsageStatePermission(checkPackageUsageStatePermission())
        } else {
            DialogRequestUsageState().show(parentFragmentManager, TAG_USAGE_STATE)
        }
    }

    private fun initScreenStateObserver() {
        lifecycleScope.launchWhenResumed {
            viewModel.screenState.collect { state ->
                renderState(state)
            }
        }
    }

    private fun renderState(state: BoostScreenState) {
        with(state) {
            setEnableBtn(state)
            setVisibility(state)
            setCountApp(state)
            setPermissionDescription(state)
            with(binding) {
                totalAmountRamCount.text = getString(R.string.gb_one_after_dot, totalRam)
                usedRamCount.text = getString(R.string.gb_one_after_dot, usedRam)
                phoneModel.text = getPhoneModel()
            }
        }
    }

    private fun BoostScreenState.setVisibility(state: BoostScreenState) {
        if (state.isRamBoosted) {
            binding.boostDone.isVisible = isRamBoosted
            binding.groupNotOptimized.isVisible = !isRamBoosted
            binding.loaderGroup.isVisible = !isRamBoosted
            binding.loadGroup.isVisible = !isRamBoosted
        } else {
            binding.loaderGroup.isVisible = !isLoadUseCase
            binding.loadGroup.isVisible = isLoadUseCase
        }
    }

    private fun setCountApp(state: BoostScreenState) {
        if (state.permissionGiven && state.isLoadUseCase) {
            binding.countApps.text = state.listBackgroundApp.size.toString()
        } else {
            binding.countApps.text = "?"
        }
    }

    private fun getPhoneModel() = Build.MANUFACTURER.toString() + " " + Build.MODEL.toString()

    private fun setEnableBtn(state: BoostScreenState) {
        binding.btnBoostBattery.isClickable = !state.isRamBoosted
        binding.btnBoostBattery.isEnabled = !state.isRamBoosted
    }

    private fun setPermissionDescription(state: BoostScreenState) {
        binding.permissionRequired.isVisible = !state.permissionGiven
    }

    private fun setBtnListeners() {
        binding.btnBoostBattery.setOnClickListener {
            if (checkPackageUsageStatePermission()) {
                findNavController().navigate(R.id.action_homeFragment_to_boostOptimizingFragment)
            } else {
                DialogRequestUsageState().show(parentFragmentManager, TAG_USAGE_STATE)
            }
        }
    }

    private fun checkPackageUsageStatePermission(): Boolean {
        val appOps = requireContext()
            .getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
        val mode = appOps.checkOpNoThrow(
            AppOpsManager.OPSTR_GET_USAGE_STATS,
            Process.myUid(), requireContext().packageName
        )
        return if (mode == AppOpsManager.MODE_DEFAULT) {
            requireContext().checkCallingOrSelfPermission(Manifest.permission.PACKAGE_USAGE_STATS) == PackageManager.PERMISSION_GRANTED
        } else {
            mode == AppOpsManager.MODE_ALLOWED
        }
    }
}