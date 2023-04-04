package com.smart.cleaner.phoneclean.ui.boost

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
import com.smart.cleaner.phoneclean.R
import com.smart.cleaner.phoneclean.databinding.FragmentBoostBinding
import com.smart.cleaner.phoneclean.ui.dialogs.DialogRequestUsageState
import com.smart.cleaner.phoneclean.ui.dialogs.DialogRequestUsageState.Companion.RESULT_USAGE_STATE
import com.smart.cleaner.phoneclean.ui.dialogs.DialogRequestUsageState.Companion.RESULT_USAGE_STATE_KEY
import com.smart.cleaner.phoneclean.ui.dialogs.DialogRequestUsageState.Companion.RESULT_USAGE_STATE_SUCCESS
import com.smart.cleaner.phoneclean.ui.dialogs.DialogRequestUsageState.Companion.TAG_USAGE_STATE
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

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
        lifecycleScope.launch {
            viewModel.screenState.collect { state ->
                renderState(state)
            }
        }
    }

    private fun renderState(state: BoostScreenState) {
        if(!state.isLoadUseCase) return
        with(state) {
            setEnableBtn(isRamBoosted)
            with(binding) {
                totalAmountRamCount.text = getString(R.string.gb_one_after_dot, totalRam)
                usedRamCount.text = getString(R.string.gb_one_after_dot, usedRam)
                phoneModel.text = getPhoneModel()
                groupNotOptimized.isVisible = !isRamBoosted
                boostDone.isVisible = isRamBoosted
                setCountApp(permissionGiven)
                setPermissionDescription(permissionGiven)
            }
        }
    }

    private fun getPhoneModel() = Build.MANUFACTURER.toString() + " " + Build.MODEL.toString()

    private fun setEnableBtn(enable: Boolean) {
        binding.btnBoostBattery.isClickable = !enable
        binding.btnBoostBattery.isEnabled = !enable
    }

    private fun setCountApp(enable: Boolean) {
        binding.countApps.text =
            if (enable) viewModel.screenState.value.listBackgroundApp.size.toString() else "?"
    }

    private fun setPermissionDescription(enable: Boolean) {
        binding.permissionRequired.isVisible = !enable
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