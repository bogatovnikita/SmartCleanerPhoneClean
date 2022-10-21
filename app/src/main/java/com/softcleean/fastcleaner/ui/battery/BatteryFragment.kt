package com.softcleean.fastcleaner.ui.battery

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
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
import com.softcleean.fastcleaner.custom.ChoosingTypeBatteryBar.Companion.INIT
import com.softcleean.fastcleaner.custom.ChoosingTypeBatteryBar.Companion.NORMAL
import com.softcleean.fastcleaner.custom.ChoosingTypeBatteryBar.Companion.ULTRA
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
            viewModel.setWriteSettingsEnabling(Settings.System.canWrite(requireContext()))
        }

    private val startActivityForResultBluetooth =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {

        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObserverStateScreen()
        viewModel.getParams()
        initAdapter()
        setTypeBoostBattery()
        setBtnListeners()
        setOnDialogsCallback()
        checkWriteSettingPermission()
    }

    private fun setOnDialogsCallback() {
        setFragmentResultListener(RESULT_WRITE_SETTING) { _, bundle ->
            if ((bundle.getString(RESULT_KEY) ?: "") == RESULT_SUCCESS) {
                requestPermForChangingBrightness()
            }
        }
    }

    private fun initObserverStateScreen() {
        viewModel.setWriteSettingsEnabling(Settings.System.canWrite(requireContext()))
        lifecycleScope.launchWhenResumed {
            viewModel.screenState.collect { state ->
                renderState(state)
            }
        }
    }

    private fun requestPermForChangingBrightness() {
        if (Settings.System.canWrite(requireContext())) return
        startActivityForResultWriteSettings.launch(Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS).apply {
            data = Uri.parse("package:" + requireActivity().packageName)
        })
    }

    private fun openActivityToDisableWifi() {
        startActivityForResultWiFi.launch(Intent(Settings.Panel.ACTION_INTERNET_CONNECTIVITY))
    }

    private fun setTypeBoostBattery() {
        binding.choosingTypeBar.setOnChangeTypeListener { type ->
            checkWriteSettingPermission()
            viewModel.setBatterySaveType(type)
            when (type) {
                NORMAL -> {
                    viewModel.setBatterySaveType(NORMAL)
                    adapter.submitList(
                        resources.getStringArray(R.array.battery_normal).toList()
                    )
                }
                ULTRA -> {
                    viewModel.setBatterySaveType(ULTRA)
                    adapter.submitList(
                        resources.getStringArray(R.array.battery_ultra).toList()
                    )
                }
                EXTRA -> {
                    viewModel.setBatterySaveType(EXTRA)
                    adapter.submitList(
                        resources.getStringArray(R.array.battery_extra).toList()
                    )
                }
            }
        }
    }

    private fun checkWriteSettingPermission() {
        if (!Settings.System.canWrite(requireContext())) {
            DialogRequestWriteSetting().show(parentFragmentManager, TAG_WRITE_SETTING)
        }
    }

    private fun renderState(batteryStateScreen: BatteryStateScreen) {
        with(batteryStateScreen) {
            with(binding) {
                tvBatteryPercents.text = getString(R.string.value_percents, batteryPercents)
                tvWorkingTime.text =
                    getString(R.string.working_time, batteryWorkingTime[0], batteryWorkingTime[1])
                if (batterySaveType == INIT) {
                    choosingTypeBar.setSaveTypeBattery(batterySaveType)
                }
                if (isBoostedBattery) {
                    viewModel.getBatterySaveType()
                    tvDangerDescriptionOff.isVisible = true
                    tvDangerDescription.isVisible = false
                    tvDangerDescriptionOff.text = getString(
                        R.string.improve_working_time_by_percent,
                        viewModel.modePercentBoost()
                    )
                } else {
                    tvDangerDescriptionOff.isVisible = false
                    tvDangerDescription.isVisible = true
                }
            }
            renderBtnBoostingBattery(isBoostedBattery, isCanWriteSettings)
            renderCircularProgressBatteryPercent(batteryPercents)
        }
    }

    private fun setBtnListeners() {
        binding.btnBoostBattery.setOnClickListener {
            viewModel.boostBattery()
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.Q &&
                (viewModel.screenState.value.batterySaveType == ULTRA ||
                viewModel.screenState.value.batterySaveType == EXTRA)
            ) {
                openActivityToDisableWifi()
            } else {
                findNavController().navigate(R.id.action_batteryFragment_to_batteryOptimizingFragment)
            }
        }
        binding.btnGoBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun renderBtnBoostingBattery(isBoostedBattery: Boolean, isCanWriteSettings: Boolean) {
        if (isBoostedBattery || !isCanWriteSettings) {
            binding.btnBoostBattery.isClickable = false
            binding.btnBoostBattery.background =
                resources.getDrawable(R.drawable.bg_button_boost_off)
        } else {
            binding.btnBoostBattery.isClickable = true
            binding.btnBoostBattery.background =
                resources.getDrawable(R.drawable.bg_button_boost_on)
        }
    }

    private fun renderCircularProgressBatteryPercent(percent: Int) {
        binding.circularProgressBatteryPercent.progress = percent.toFloat()
        binding.circularProgressBatteryPercent.indicator.color =
            if (percent > 40)
                resources.getColor(R.color.blue)
            else if (percent > 20)
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