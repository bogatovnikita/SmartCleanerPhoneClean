package com.softcleean.fastcleaner.ui.clean

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.softcleean.fastcleaner.R
import com.softcleean.fastcleaner.databinding.FragmentCleanResultBinding
import com.softcleean.fastcleaner.ui.result.BaseResultFragment
import com.softcleean.fastcleaner.ui.result.ResultList
import com.softcleean.fastcleaner.utils.OptimizingType
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
                renderCircularProgress(isCleared)
                tvStoragePercents.text = getString(R.string.value_percents, memoryPercent)
                tvFreeMemory.text = getString(R.string.free_memory_size, freeMemory)
            }
        }
    }

    private fun renderCircularProgress(isBoosted: Boolean) {
        binding.circularProgressStoragePercent.indicator.color =
            if (isBoosted)
                resources.getColor(R.color.blue)
            else
                resources.getColor(R.color.orange)
    }
}