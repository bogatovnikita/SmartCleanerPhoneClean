package com.softcleean.fastcleaner.ui.battery

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
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.softcleean.fastcleaner.R
import com.softcleean.fastcleaner.databinding.FragmentBatteryBinding
import com.softcleean.fastcleaner.adapters.BatterySaveFunctionRecyclerViewAdapter
import com.softcleean.fastcleaner.custom.ChoosingTypeBatteryBar.Companion.EXTRA
import com.softcleean.fastcleaner.custom.ChoosingTypeBatteryBar.Companion.NORMAL
import com.softcleean.fastcleaner.custom.ChoosingTypeBatteryBar.Companion.ULTRA
import com.softcleean.fastcleaner.ui.dialogs.DialogRequestBluetooth
import com.softcleean.fastcleaner.ui.dialogs.DialogRequestBluetooth.Companion.RESULT_KEY_BLUETOOTH
import com.softcleean.fastcleaner.ui.dialogs.DialogRequestBluetooth.Companion.RESULT_SUCCESS_BLUETOOTH
import com.softcleean.fastcleaner.ui.dialogs.DialogRequestBluetooth.Companion.TAG_BLUETOOTH
import com.softcleean.fastcleaner.ui.dialogs.DialogRequestWriteSetting
import com.softcleean.fastcleaner.ui.dialogs.DialogRequestWriteSetting.Companion.RESULT_KEY
import com.softcleean.fastcleaner.ui.dialogs.DialogRequestWriteSetting.Companion.RESULT_SUCCESS
import com.softcleean.fastcleaner.ui.dialogs.DialogRequestWriteSetting.Companion.RESULT_WRITE_SETTING
import com.softcleean.fastcleaner.ui.dialogs.DialogRequestWriteSetting.Companion.TAG_WRITE_SETTING
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BatteryFragment : Fragment(R.layout.fragment_battery) {

    private val viewModel: BatteryViewModel by viewModels()

    private val binding: FragmentBatteryBinding by viewBinding()

    private lateinit var adapter: BatterySaveFunctionRecyclerViewAdapter

    private val startActivityForResultWiFi =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            findNavController().navigate(R.id.action_batteryFragment_to_batteryOptimizingFragment)
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


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObserverStateScreen()
        viewModel.getParams()
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
        if (hasPermCanWrite()) return
        startActivityForResultWriteSettings.launch(Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS).apply {
            data = Uri.parse("package:" + requireActivity().packageName)
        })
    }

    private fun hasPermCanWrite(): Boolean =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) Settings.System.canWrite(requireContext()) else true

    private fun requestPermForBluetooth() {
        startActivityForResultBluetooth.launch(arrayOf(Manifest.permission.BLUETOOTH_CONNECT))
    }

    private fun openActivityToDisableWifi() {
        startActivityForResultWiFi.launch(Intent(Settings.Panel.ACTION_INTERNET_CONNECTIVITY))
    }

    private fun setTypeBoostBattery() {
        binding.choosingTypeBar.setOnChangeTypeListener { type ->
            checkPermissions()
            viewModel.setBatterySaveType(type)
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
            batterySavingTypeProcessing(batterySaveType)
            with(binding) {
                choosingTypeBar.setSaveTypeBattery(batterySaveType)
                tvBatteryPercents.text = getString(R.string.value_percents, batteryPercents)
                if (hasBluetoothPerm) {
                    descriptionGoSettings.isVisible = false
                }
            }
            renderBtnBoostingBattery(isBoostedBattery, isCanWriteSettings, hasBluetoothPerm)
            renderCircularProgressBatteryPercent(batteryPercents, isBoostedBattery)
        }
    }

    private fun batterySavingTypeProcessing(type: String) {
        when (type) {
            NORMAL -> {
                adapter.submitList(
                    resources.getStringArray(R.array.battery_normal).toList()
                )
                binding.descriptionGoSettings.isVisible = false
            }
            ULTRA -> {
                adapter.submitList(
                    resources.getStringArray(R.array.battery_ultra).toList()
                )
                binding.descriptionGoSettings.isVisible = false
            }
            EXTRA -> {
                adapter.submitList(
                    resources.getStringArray(R.array.battery_extra).toList()
                )
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
        }
    }

    private fun setBtnListeners() {
        binding.btnBoostBattery.setOnClickListener {
            viewModel.boostBattery()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q &&
                (viewModel.screenState.value.batterySaveType == ULTRA ||
                        viewModel.screenState.value.batterySaveType == EXTRA)
            ) {
                openActivityToDisableWifi()
            } else {
                findNavController().navigate(R.id.action_batteryFragment_to_batteryOptimizingFragment)
            }
        }
    }

    private fun renderBtnBoostingBattery(
        isBoostedBattery: Boolean,
        isCanWriteSettings: Boolean,
        hasBluetoothPerm: Boolean
    ) {
        if (isBoostedBattery || !isCanWriteSettings) {
            binding.btnBoostBattery.isClickable = false
            binding.btnBoostBattery.background =
                resources.getDrawable(R.drawable.bg_button_boost_off)
        } else if (!hasBluetoothPerm && viewModel.screenState.value.batterySaveType == EXTRA) {
            binding.btnBoostBattery.isClickable = false
            binding.btnBoostBattery.background =
                resources.getDrawable(R.drawable.bg_button_boost_off)
        } else {
            binding.btnBoostBattery.isClickable = true
            binding.btnBoostBattery.background =
                resources.getDrawable(R.drawable.bg_button_boost_on)
        }
    }

    private fun renderCircularProgressBatteryPercent(percent: Int, isBoostedBattery: Boolean) {
        binding.circularProgressBatteryPercent.progress = percent.toFloat()
        binding.circularProgressBatteryPercent.indicator.color =
            if (isBoostedBattery)
                resources.getColor(R.color.blue)
            else if (percent > 30)
                resources.getColor(R.color.orange)
            else
                resources.getColor(R.color.red)
    }

    private fun initAdapter() {
        adapter = BatterySaveFunctionRecyclerViewAdapter()
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter.submitList(resources.getStringArray(R.array.battery_normal).toList())
    }
}