package com.entertainment.event.ssearch.ar155.functions.clean

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.entertainment.event.ssearch.ar155.R
import com.entertainment.event.ssearch.ar155.adapters.CleanAdapter
import com.entertainment.event.ssearch.ar155.databinding.FragmentCleanBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CleanFragment : Fragment(R.layout.fragment_clean) {

    private val binding: FragmentCleanBinding by viewBinding()

    private val viewModel: CleanViewModel by viewModels()

    private lateinit var adapter: CleanAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getGarbageInfo()
        initAdapter()
        initScreenStateObserver()
        goToOptimizingFragment()
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
            adapter.submitList(garbageList)
            renderBtnClean(isCleared)
            binding.apply {
                tvDegree.text = getString(R.string.mb, totalGarbageSize)
            }
        }
    }

    private fun renderBtnClean(isBoostedBattery: Boolean) {
        if (isBoostedBattery) {
            binding.btnClean.isClickable = false
            binding.btnClean.background = resources.getDrawable(R.drawable.bg_button_boost_off)
        } else {
            binding.btnClean.isClickable = true
            binding.btnClean.background = resources.getDrawable(R.drawable.bg_button_boost_on)
        }
    }

    private fun goToOptimizingFragment() {
        binding.btnClean.setOnClickListener {
            viewModel.cleanGarbage()
            findNavController().navigate(R.id.action_cleanFragment_to_cleanOptimizingFragment)
        }
    }

    private fun initAdapter() {
        adapter = CleanAdapter()
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
    }
}
