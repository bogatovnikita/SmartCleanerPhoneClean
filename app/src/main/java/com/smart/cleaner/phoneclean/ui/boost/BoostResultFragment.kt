package com.smart.cleaner.phoneclean.ui.boost

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.smart.cleaner.phoneclean.R
import com.smart.cleaner.phoneclean.databinding.FragmentBoostResultBinding
import com.smart.cleaner.phoneclean.ui.base.BaseFragmentResult
import com.smart.cleaner.phoneclean.ui.result.FunResult
import com.smart.cleaner.phoneclean.ui.result.ResultList
import com.smart.cleaner.phoneclean.utils.OptimizingType
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class BoostResultFragment : BaseFragmentResult(R.layout.fragment_boost_result) {

    private val binding: FragmentBoostResultBinding by viewBinding()

    @Inject
    lateinit var resultList: ResultList

    private val viewModel: BoostViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initScreenStateObserver()
        initListeners()
    }

    private fun initScreenStateObserver() {
        lifecycleScope.launch {
            viewModel.screenState.collect { state ->
                renderState(state)
            }
        }
    }

    private fun renderState(state: BoostScreenState) {
        with(state) {
            with(binding) {
                circularProgressRamPercentDuplicate.progress = ramPercent.toFloat()
                tvRamPercentsDuplicate.text = getString(R.string.value_percents, ramPercent)
            }
        }
    }

    private fun initListeners() {
        binding.btnGoBack.setOnClickListener { findNavController().popBackStack() }
    }

    override fun setListFun(): List<FunResult> =
        resultList.getList().filter { it.type != OptimizingType.Boost }

    override fun setRecyclerView(): RecyclerView = binding.recyclerView

}