package com.softcleean.fastcleaner.ui.cooling

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.softcleean.fastcleaner.R
import com.softcleean.fastcleaner.databinding.FragmentCoolingResultBinding
import com.softcleean.fastcleaner.ui.result.BaseResultFragment
import com.softcleean.fastcleaner.ui.result.ResultList
import com.softcleean.fastcleaner.utils.OptimizingType
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class CoolingResultFragment :
    BaseResultFragment<FragmentCoolingResultBinding>(FragmentCoolingResultBinding::inflate) {

    private val viewModel: CoolingViewModel by viewModels()

    @Inject
    lateinit var resultList: ResultList

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapter(recyclerView = binding.recyclerView)
        viewModel.getTemperature()
        initScreenStateObserver()
        setBtnListeners(btnGoBack = binding.btnGoBack, btnGoMain = binding.btnGoMain)
    }

    private fun initScreenStateObserver() {
        lifecycleScope.launchWhenResumed {
            viewModel.screenState.collect { state ->
                renderState(state)
            }
        }
    }

    private fun renderState(stateScreen: CoolingScreenState) {
        with(stateScreen) {
            with(binding) {
                tvDegree.text = getString(R.string.degree, temperature)
                tvDangerDescriptionOff.isVisible = true
                adapter.submitList(resultList.getList().filter {
                    OptimizingType.Cooling != it.type
                })
            }
        }
    }
}