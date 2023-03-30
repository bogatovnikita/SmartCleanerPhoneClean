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
import com.smart.cleaner.phoneclean.ui_core.adapters.base.BaseFragmentResult
import com.smart.cleaner.phoneclean.ui_core.adapters.models.OptimizingType
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class BoostResultFragment(
    override val typeResult: OptimizingType = OptimizingType.Boost
) : BaseFragmentResult(R.layout.fragment_boost_result) {

    private val binding: FragmentBoostResultBinding by viewBinding()

    private val viewModel: BoostViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getParams()
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
                tvRamPercentsDuplicate.text = getString(general.R.string.value_percents, ramPercent)
            }
        }
    }

    private fun initListeners() {
        binding.btnGoBack.setOnClickListener { findNavController().popBackStack() }
    }

    override fun setRecyclerView(): RecyclerView = binding.recyclerView

}