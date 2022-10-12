package com.entertainment.event.ssearch.ar155.functions.clean

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.entertainment.event.ssearch.ar155.R
import com.entertainment.event.ssearch.ar155.databinding.FragmentCleanResultBinding
import com.entertainment.event.ssearch.ar155.functions.result.BaseResultFragment
import com.entertainment.event.ssearch.ar155.functions.result.ResultList
import com.entertainment.event.ssearch.ar155.utils.OptimizingType
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class CleanResultFragment :
    BaseResultFragment<FragmentCleanResultBinding>(FragmentCleanResultBinding::inflate) {

    private val viewModel: CleanViewModel by viewModels()

    @Inject
    lateinit var resultList: ResultList

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapter(recyclerView = binding.recyclerView)
        setBtnListeners(btnGoBack = binding.btnGoBack, btnGoMain = binding.btnGoMain)
        viewModel.getGarbageInfo()
        initScreenStateObserver()
    }

    private fun initScreenStateObserver() {
        lifecycleScope.launch {
            viewModel.screenState.collect { state ->
                renderState(state)
            }
        }
    }

    private fun renderState(stateScreen: CleanStateScreen) {
        with(stateScreen) {
            adapter.submitList(resultList.getList().filter {
                OptimizingType.Clean != it.type
            })
            with(binding) {
                circularProgressStoragePercent.progress = memoryPercent.toFloat()
                renderCircularProgress(memoryPercent)
                tvStoragePercents.text = getString(R.string.value_percents, memoryPercent)
                tvUsedStorage.text = getString(R.string.gb, usedMemory)
                tvTotalStorage.text = getString(R.string.gb_fraction, totalMemory)
                tvFreeStorage.text = getString(R.string.gb, freeMemory)
            }
        }
    }

    private fun renderCircularProgress(percent: Int) {
        binding.circularProgressStoragePercent.indicator.color =
            if (percent > 85)
                resources.getColor(R.color.red)
            else if (percent > 60)
                resources.getColor(R.color.orange)
            else
                resources.getColor(R.color.blue)
    }
}