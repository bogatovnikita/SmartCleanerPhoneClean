package com.smart.cleaner.phone.clean.ui.battery

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.smart.cleaner.phone.clean.R
import com.smart.cleaner.phone.clean.databinding.FragmentBatteryBinding
import com.smart.cleaner.phone.clean.adapters.BatterySaveFunctionRecyclerViewAdapter
import com.smart.cleaner.phone.clean.custom.ChoosingTypeBatteryBar.Companion.EXTRA
import com.smart.cleaner.phone.clean.custom.ChoosingTypeBatteryBar.Companion.NORMAL
import com.smart.cleaner.phone.clean.custom.ChoosingTypeBatteryBar.Companion.ULTRA
import com.smart.cleaner.phone.clean.ui.dialogs.DialogRequestBluetooth
import com.smart.cleaner.phone.clean.ui.dialogs.DialogRequestBluetooth.Companion.RESULT_KEY_BLUETOOTH
import com.smart.cleaner.phone.clean.ui.dialogs.DialogRequestBluetooth.Companion.RESULT_SUCCESS_BLUETOOTH
import com.smart.cleaner.phone.clean.ui.dialogs.DialogRequestBluetooth.Companion.TAG_BLUETOOTH
import com.smart.cleaner.phone.clean.ui.dialogs.DialogRequestWriteSetting
import com.smart.cleaner.phone.clean.ui.dialogs.DialogRequestWriteSetting.Companion.RESULT_KEY
import com.smart.cleaner.phone.clean.ui.dialogs.DialogRequestWriteSetting.Companion.RESULT_SUCCESS
import com.smart.cleaner.phone.clean.ui.dialogs.DialogRequestWriteSetting.Companion.RESULT_WRITE_SETTING
import com.smart.cleaner.phone.clean.ui.dialogs.DialogRequestWriteSetting.Companion.TAG_WRITE_SETTING
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class BatteryFragment : Fragment(R.layout.fragment_battery) {

    private val viewModel: BatteryViewModel by activityViewModels()

    private val binding: FragmentBatteryBinding by viewBinding()

    private lateinit var adapter: BatterySaveFunctionRecyclerViewAdapter

    private val startActivityForResultWiFi =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            findNavController().navigate(R.id.action_batteryFragment_to_batteryOptimizingFragment)
            updateState()
        }

    private val startActivityForResultWriteSettings =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            viewModel.setWriteSettingsEnabling(hasPermCanWrite())
        }

    private val startActivityForResultBluetooth =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
            if (it.isNotEmpty() && !it.containsValue(false)) {
                viewModel.setHasBluetoothPerm(true)
            }
        }

    private val startActivityForResultBluetoothSettings =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            viewModel.setHasBluetoothPerm(checkBluetoothPermission())
        }

    override fun onResume() {
        super.onResume()
        viewModel.getParams()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObserverStateScreen()
        initAdapter()
        setTypeBoostBattery()
        setBtnListeners()
        setOnDialogsCallback()
        checkPermissions()
    }

    private fun initObserverStateScreen() {
        viewModel.setWriteSettingsEnabling(hasPermCanWrite())
        lifecycleScope.launchWhenResumed {
            viewModel.screenState.collect { state ->
                renderState(state)
            }
        }
    }

    private fun setOnDialogsCallback() {
        setFragmentResultListener(RESULT_WRITE_SETTING) { _, bundle ->
            if ((bundle.getString(RESULT_KEY) ?: "") == RESULT_SUCCESS) {
                requestPermForChangingBrightness()
            }
            if ((bundle.getString(RESULT_KEY_BLUETOOTH) ?: "") == RESULT_SUCCESS_BLUETOOTH) {
                requestPermForBluetooth()
            }
        }
    }

    private fun requestPermForChangingBrightness() {
        startActivityForResultWriteSettings.launch(Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS).apply {
            data = Uri.parse("package:" + requireActivity().packageName)
        })
    }

    private fun hasPermCanWrite(): Boolean =
        Settings.System.canWrite(requireContext())

    private fun requestPermForBluetooth() {
        startActivityForResultBluetooth.launch(arrayOf(Manifest.permission.BLUETOOTH_CONNECT))
    }

    private fun openActivityToDisableWifi() {
        startActivityForResultWiFi.launch(Intent(Settings.Panel.ACTION_INTERNET_CONNECTIVITY))
    }

    private fun setTypeBoostBattery() {
        binding.choosingTypeBar.setOnChangeTypeListener { type ->
            viewModel.setCurrentBatteryType(type)
            checkPermissions()
        }
    }

    private fun checkPermissions() {
        if (!hasPermCanWrite()) {
            DialogRequestWriteSetting().show(parentFragmentManager, TAG_WRITE_SETTING)
        }
        viewModel.setHasBluetoothPerm(checkBluetoothPermission())
    }

    private fun checkBluetoothPermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
            checkSelfPermission(
                requireContext(),
                Manifest.permission.BLUETOOTH_CONNECT
            ) == PERMISSION_GRANTED
        else Build.VERSION.SDK_INT < Build.VERSION_CODES.S
    }

    private fun renderState(batteryStateScreen: BatteryStateScreen) {
        with(batteryStateScreen) {
            setBatteryStatus(isBoostedBattery, batterySaveType)
            setBatteryTypeProcessing(currentBatteryType)
            with(binding) {
                if (hasBluetoothPerm) {
                    descriptionGoSettings.isVisible = false
                }
                permissionRequired.isVisible = !isCanWriteSettings
            }
            renderBtnBoostingBattery(isCanWriteSettings, hasBluetoothPerm, currentBatteryType)
            renderBtnSetBrightness(batteryStateScreen)
        }
    }

    private fun renderBtnSetBrightness(state: BatteryStateScreen) {
        val isVisible = state.currentBatteryType == state.batterySaveType && state.isNeedShowBtnSetBrightness
        binding.btnBoostBattery.isVisible = !isVisible
        binding.btnSetBrightness.isVisible = isVisible
    }

    private fun setBatteryStatus(isBoostedBattery: Boolean, batterySaveType: String) {
        val ivId = if (isBoostedBattery) R.drawable.battery_normal else R.drawable.battery_low
        binding.ivBatteryStatus.setImageResource(ivId)
        binding.tvBatteryStatus.isVisible = isBoostedBattery
        if (isBoostedBattery) {
            val descriptionStrId = when (batterySaveType) {
                NORMAL -> R.string.normal_mode_activated
                ULTRA -> R.string.ultra_mode_activated
                EXTRA -> R.string.extra_mode_activated
                else -> R.string.normal_mode_activated
            }
            binding.tvBatteryStatus.text = getString(descriptionStrId)
        }
    }

    private fun setBatteryTypeProcessing(currentBatteryType: String) {
        binding.choosingTypeBar.setTypeBattery(currentBatteryType)
        when (currentBatteryType) {
            NORMAL -> {
                renderTypeStatus(R.array.battery_normal, R.string.type_description_normal)
                binding.descriptionGoSettings.isVisible = false
            }
            ULTRA -> {
                renderTypeStatus(R.array.battery_ultra, R.string.type_description_ultra)
                binding.descriptionGoSettings.isVisible = false
                showDialogOrRequestBLEPermission()
            }
            EXTRA -> {
                val array =
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) R.array.battery_extra_manually else R.array.battery_extra
                renderTypeStatus(array, R.string.type_description_extra)
                showDialogOrRequestBLEPermission()
            }
        }
    }

    private fun showDialogOrRequestBLEPermission() {
        if (!checkBluetoothPermission()) {
            if (shouldShowRequestPermissionRationale(Manifest.permission.BLUETOOTH_CONNECT)) {
                binding.descriptionGoSettings.isVisible = true
                binding.btnGoBluetoothSettings.setOnClickListener {
                    startActivityForResultBluetoothSettings.launch(Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                        data = Uri.parse("package:" + requireActivity().packageName)
                    })
                }
            } else {
                DialogRequestBluetooth().show(parentFragmentManager, TAG_BLUETOOTH)
            }
        }
    }

    private fun renderTypeStatus(list: Int, type: Int) {
        adapter.submitList(
            resources.getStringArray(list).toList()
        )
        binding.titleList.text = getText(type)
    }

    private fun setBtnListeners() {
        binding.btnBoostBattery.setOnClickListener {
            viewModel.boostBattery()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q && viewModel.screenState.value.currentBatteryType == EXTRA) {
                openActivityToDisableWifi()
            } else {
                findNavController().navigate(R.id.action_batteryFragment_to_batteryOptimizingFragment)
                updateState()
            }
        }
        binding.btnSetBrightness.setOnClickListener {
            viewModel.setBrightnessTo80()
        }
    }

    private fun renderBtnBoostingBattery(
        isCanWriteSettings: Boolean,
        hasBluetoothPerm: Boolean,
        currentBatteryType: String
    ) {
        if (!isCanWriteSettings) {
            makeAvailableBtn(false)
            binding.btnBoostBattery.isVisible = true
        } else if (!hasBluetoothPerm && (currentBatteryType == EXTRA || currentBatteryType == ULTRA)) {
            makeAvailableBtn(false)
            if (shouldShowRequestPermissionRationale(Manifest.permission.BLUETOOTH_CONNECT))
                binding.btnBoostBattery.isVisible = false
        } else {
            makeAvailableBtn(true)
            binding.btnBoostBattery.isVisible = true
        }
    }

    private fun makeAvailableBtn(enable: Boolean) {
        binding.btnBoostBattery.isClickable = enable
        binding.btnBoostBattery.isEnabled = enable
    }

    private fun initAdapter() {
        adapter = BatterySaveFunctionRecyclerViewAdapter()
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter.submitList(resources.getStringArray(R.array.battery_normal).toList())
    }

    private fun updateState() {
        lifecycleScope.launch {
            delay(1000)
            viewModel.getParams()
        }
    }
}