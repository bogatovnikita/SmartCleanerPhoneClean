package com.softcleean.fastcleaner.functions.battery

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.softcleean.fastcleaner.R
import com.softcleean.fastcleaner.databinding.FragmentBatteryResultBinding
import com.softcleean.fastcleaner.functions.result.BaseResultFragment
import com.softcleean.fastcleaner.functions.result.ResultList
import com.softcleean.fastcleaner.utils.OptimizingType
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class BatteryResultFragment :
    BaseResultFragment<FragmentBatteryResultBinding>(FragmentBatteryResultBinding::inflate) {

    private val viewModel: BatteryViewModel by viewModels()

    @Inject
    lateinit var resultList: ResultList

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapter(recyclerView = binding.recyclerView)
        viewModel.getParams()
        initScreenStateObserver()
        setBtnListeners(btnGoBack = binding.btnGoBack, btnGoMain = binding.btnGoMain)
    }

    private fun initScreenStateObserver() {
        lifecycleScope.launch {
            viewModel.screenState.collect { state ->
                renderState(state)
            }
        }
    }

    private fun renderState(stateScreen: BatteryStateScreen) {
        with(stateScreen) {
            adapter.submitList(resultList.getList().filter {
                OptimizingType.Battery != it.type
            })
            with(binding) {
                tvBatteryPercents.text = getString(R.string.value_percents, batteryPercents)
                tvWorkingTime.text =
                    getString(R.string.working_time, batteryWorkingTime[0], batteryWorkingTime[1])
                tvDangerDescriptionOff.text = getString(R.string.improve_working_time_by_percent, viewModel.modePercentBoost())
                circularProgressBatteryPercent.progress = batteryPercents.toFloat()
                renderCircularProgressBatteryPercent(batteryPercents)
            }
        }
    }

    private fun renderCircularProgressBatteryPercent(percent: Int) {
        binding.circularProgressBatteryPercent.indicator.color =
            if (percent > 40)
                resources.getColor(R.color.blue)
            else if (percent > 20)
                resources.getColor(R.color.orange)
            else
                resources.getColor(R.color.red)
    }

}