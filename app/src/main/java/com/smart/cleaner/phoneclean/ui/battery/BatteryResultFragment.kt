package com.smart.cleaner.phoneclean.ui.battery

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.smart.cleaner.phoneclean.R
import com.smart.cleaner.phoneclean.custom.ChoosingTypeBatteryBar
import com.smart.cleaner.phoneclean.databinding.FragmentBatteryResultBinding
import com.smart.cleaner.phoneclean.ui_core.adapters.base.BaseFragmentResult
import com.smart.cleaner.phoneclean.ui_core.adapters.models.OptimizingType
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class BatteryResultFragment(
    override val typeResult: OptimizingType = OptimizingType.Battery
) : BaseFragmentResult(R.layout.fragment_battery_result) {

    private val binding: FragmentBatteryResultBinding by viewBinding()

    private val viewModel: BatteryViewModel by viewModels()

    override fun setRecyclerView(): RecyclerView = binding.recyclerView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getParams()
        initScreenStateObserver()
    }

    private fun initScreenStateObserver() {
        lifecycleScope.launch {
            viewModel.screenState.collect { state ->
                setDescription(state)
            }
        }
    }

    private fun setDescription(state: BatteryStateScreen) {
        val descriptionStrId = when (state.currentBatteryType) {
            ChoosingTypeBatteryBar.NORMAL -> R.string.normal_mode_activated
            ChoosingTypeBatteryBar.ULTRA -> R.string.ultra_mode_activated
            ChoosingTypeBatteryBar.EXTRA -> R.string.extra_mode_activated
            else -> R.string.normal_mode_activated
        }
        binding.tvOptimized.text = getString(descriptionStrId)
    }

}