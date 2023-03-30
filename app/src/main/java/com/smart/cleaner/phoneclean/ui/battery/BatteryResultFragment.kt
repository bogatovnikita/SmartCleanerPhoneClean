package com.smart.cleaner.phoneclean.ui.battery

import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.smart.cleaner.phoneclean.R
import com.smart.cleaner.phoneclean.databinding.FragmentBatteryResultBinding
import com.smart.cleaner.phoneclean.ui_core.adapters.base.BaseFragmentResult
import com.smart.cleaner.phoneclean.ui_core.adapters.models.OptimizingType

class BatteryResultFragment(
    override val typeResult: OptimizingType = OptimizingType.Battery
) : BaseFragmentResult(R.layout.fragment_battery_result) {

    private val binding: FragmentBatteryResultBinding by viewBinding()

    override fun setRecyclerView(): RecyclerView = binding.recyclerView

}