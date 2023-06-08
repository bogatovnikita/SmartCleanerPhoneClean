package com.smart.cleaner.phone.clean.ui.boost

import android.Manifest
import android.app.AppOpsManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
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
import com.smart.cleaner.phone.clean.R
import com.smart.cleaner.phone.clean.databinding.FragmentBoostBinding
import com.smart.cleaner.phone.clean.ui.boost.BoostResultFragment.Companion.BOOST_FRAGMENT_KEY
import com.smart.cleaner.phone.clean.ui.dialogs.DialogRequestUsageState
import com.smart.cleaner.phone.clean.ui.dialogs.DialogRequestUsageState.Companion.RESULT_USAGE_STATE
import com.smart.cleaner.phone.clean.ui.dialogs.DialogRequestUsageState.Companion.RESULT_USAGE_STATE_KEY
import com.smart.cleaner.phone.clean.ui.dialogs.DialogRequestUsageState.Companion.RESULT_USAGE_STATE_SUCCESS
import com.smart.cleaner.phone.clean.ui.dialogs.DialogRequestUsageState.Companion.TAG_USAGE_STATE
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class BoostFragment : Fragment(R.layout.fragment_boost) {

    @Inject
    lateinit var settings: com.smart.cleaner.phoneclean.settings.Settings

    private val binding: FragmentBoostBinding by viewBinding()

    private val viewModel: BoostViewModel by viewModels()

    private val startActivityForResultUsageState =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            viewModel.setUsageStatePermission(checkPackageUsageStatePermission())
        }

    override fun onResume() {
        super.onResume()
        checkPermission()
        viewModel.getParams()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        callBackCloseDialog()
        setOnDialogCallback()
        initScreenStateObserver()
        setBtnListeners()
    }

    private fun callBackCloseDialog() {
        parentFragmentManager.setFragmentResultListener(BOOST_FRAGMENT_KEY, this) { _, _ ->
            viewModel.getParams()
        }
    }

    private fun setOnDialogCallback() {
        setFragmentResultListener(RESULT_USAGE_STATE) { _, bundle ->
            if ((bundle.getString(RESULT_USAGE_STATE_KEY) ?: "") == RESULT_USAGE_STATE_SUCCESS) {
                requestUsageStatePermission()
            }
        }
    }

    private fun requestUsageStatePermission() {
        val intent = Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS)
        val packageUri = Uri.parse("package:" + requireActivity().packageName)
        intent.data = packageUri
        if (intent.resolveActivity(requireActivity().packageManager) != null) {
            startActivityForResultUsageState.launch(intent)
        } else {
            startActivityForResultUsageState.launch(Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS))
        }
    }

    private fun checkPermission() {
        if (settings.getOpenInformationDialog()) return
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
            setVisibility()
            setCountApp(state)
            setPermissionDescription(state)
            with(binding) {
                totalAmountRamCount.text = getString(R.string.gb_one_after_dot, totalRam)
                usedRamCount.text = getString(R.string.gb_one_after_dot, usedRam)
                phoneModel.text = getPhoneModel()
            }
        }
    }

    private fun setEnableBtn(state: BoostScreenState) {
        val isVisible = if (state.isRamBoosted) false else !state.isNothingToKill
        binding.btnBoostBattery.isClickable = isVisible
        binding.btnBoostBattery.isEnabled = isVisible
    }

    private fun BoostScreenState.setVisibility() {
        binding.groupBoosted.isVisible = isRamBoosted
        binding.loaderGroup.isVisible = !isLoadUseCase && !isRamBoosted
        if (isRamBoosted) return
        binding.loadGroup.isVisible = isLoadUseCase
        binding.loadGroupEmpty.isVisible = isLoadUseCase && isNothingToKill
        binding.loadGroupNotEmpty.isVisible = isLoadUseCase && !isNothingToKill
    }

    private fun setCountApp(state: BoostScreenState) {
        if (state.isPermissionGiven && state.isLoadUseCase) {
            binding.countApps.text = state.listBackgroundApp.size.toString()
        } else {
            binding.countApps.text = "?"
        }
    }

    private fun getPhoneModel() = Build.MANUFACTURER.toString() + " " + Build.MODEL.toString()

    private fun setPermissionDescription(state: BoostScreenState) {
        binding.permissionRequired.isVisible = !state.isPermissionGiven
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