package com.entertainment.event.ssearch.ar155.functions.clean

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.entertainment.event.ssearch.ar155.R
import com.entertainment.event.ssearch.ar155.databinding.FragmentCleanResultBinding
import com.entertainment.event.ssearch.ar155.functions.result.BaseResultFragment
import com.entertainment.event.ssearch.ar155.functions.result.ResultList
import com.entertainment.event.ssearch.ar155.utils.LOW_LEVEL
import com.entertainment.event.ssearch.ar155.utils.MEDIUM_LEVEL
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
                tvFreeMemory.text = getString(R.string.free_memory_size, freeMemory)
            }
        }
    }

    private fun renderCircularProgress(percent: Int) {
        binding.circularProgressStoragePercent.indicator.color =
            if (percent > LOW_LEVEL)
                resources.getColor(R.color.red)
            else if (percent > MEDIUM_LEVEL)
                resources.getColor(R.color.orange)
            else
                resources.getColor(R.color.blue)
    }
}