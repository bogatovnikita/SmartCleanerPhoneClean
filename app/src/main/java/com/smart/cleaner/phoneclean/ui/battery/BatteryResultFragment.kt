package com.smart.cleaner.phoneclean.ui.battery

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.smart.cleaner.phoneclean.R
import com.smart.cleaner.phoneclean.databinding.FragmentBatteryResultBinding
import com.smart.cleaner.phoneclean.ui.result.ResultList
import com.smart.cleaner.phoneclean.ui_core.adapters.base.BaseFragmentResult
import com.smart.cleaner.phoneclean.ui_core.adapters.models.FunResult
import com.smart.cleaner.phoneclean.ui_core.adapters.models.OptimizingType
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class BatteryResultFragment : BaseFragmentResult(R.layout.fragment_battery_result) {

    private val binding: FragmentBatteryResultBinding by viewBinding()

    @Inject
    lateinit var resultList: ResultList

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListeners()
    }

    private fun initListeners() {
        binding.btnGoBack.setOnClickListener { findNavController().popBackStack() }
    }

    override fun setListFun(): List<FunResult> =
        resultList.getList().filter { it.type != OptimizingType.Battery }

    override fun setRecyclerView(): RecyclerView = binding.recyclerView

}