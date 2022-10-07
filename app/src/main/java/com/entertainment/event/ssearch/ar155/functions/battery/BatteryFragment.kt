package com.entertainment.event.ssearch.ar155.functions.battery

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.entertainment.event.ssearch.ar155.R
import com.entertainment.event.ssearch.ar155.databinding.FragmentBatteryBinding
import com.entertainment.event.ssearch.ar155.adapters.BatterySaveFunctionRecyclerViewAdapter
import com.entertainment.event.ssearch.ar155.functions.custom.ChoosingTypeBatteryBar.Companion.EXTRA
import com.entertainment.event.ssearch.ar155.functions.custom.ChoosingTypeBatteryBar.Companion.NORMAL
import com.entertainment.event.ssearch.ar155.functions.custom.ChoosingTypeBatteryBar.Companion.ULTRA
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BatteryFragment : Fragment(R.layout.fragment_battery) {

    private val viewModel: BatteryViewModel by viewModels()

    private val binding: FragmentBatteryBinding by viewBinding()

    private lateinit var adapter: BatterySaveFunctionRecyclerViewAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObserverStateScreen()
        viewModel.getParams()
        initAdapter()
        setTypeBoostBattery()
        goToBoostingBatteryListener()
    }

    private fun initObserverStateScreen() {
        lifecycleScope.launchWhenResumed {
            viewModel.screenState.collect { state ->
                renderState(state)
            }
        }
    }

    private fun setTypeBoostBattery() {
        binding.choosingTypeBar.setOnChangeTypeListener { type ->
            viewModel.setBatterySaveType(type)
            when (type) {
                NORMAL -> adapter.submitList(
                    resources.getStringArray(R.array.battery_normal).toList()
                )
                ULTRA -> adapter.submitList(
                    resources.getStringArray(R.array.battery_ultra).toList()
                )
                EXTRA -> adapter.submitList(
                    resources.getStringArray(R.array.battery_extra).toList()
                )
            }
        }
    }

    private fun renderBtnBoostingBattery(isBoostedBattery: Boolean) {
        if (isBoostedBattery) {
            binding.btnBoostBattery.isClickable = false
            binding.btnBoostBattery.background = resources.getDrawable(R.drawable.bg_button_boost_off)
        } else {
            binding.btnBoostBattery.isClickable = true
            binding.btnBoostBattery.background = resources.getDrawable(R.drawable.bg_button_boost_on)
        }
    }

    private fun renderState(batteryStateScreen: BatteryStateScreen) {
        with(batteryStateScreen) {
            with(binding) {
                tvBatteryPercents.text = getString(R.string.value_percents, batteryPercents)
                tvWorkingTime.text =
                    getString(R.string.working_time, batteryWorkingTime[0], batteryWorkingTime[1])
                choosingTypeBar.setSaveTypeBattery(batterySaveType)
                if (isBoostedBattery) {
                    viewModel.getBatterySaveType()
                    tvDangerDescriptionOff.isVisible = true
                    tvDangerDescription.isVisible = false
                    tvDangerDescriptionOff.text = getString(R.string.improve_working_time_by_percent, viewModel.modePercentBoost())
                } else {
                    tvDangerDescriptionOff.isVisible = false
                    tvDangerDescription.isVisible = true
                }
            }
            renderBtnBoostingBattery(isBoostedBattery)
            renderCircularProgressBatteryPercent(batteryPercents)
        }
    }

    private fun goToBoostingBatteryListener() {
        binding.btnBoostBattery.setOnClickListener {
            viewModel.boostBattery()
            findNavController().navigate(R.id.action_batteryFragment_to_batteryOptimizingFragment)
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