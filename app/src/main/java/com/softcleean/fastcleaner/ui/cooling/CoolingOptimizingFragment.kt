package com.softcleean.fastcleaner.ui.cooling

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.activity.addCallback
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.softcleean.fastcleaner.BuildConfig
import com.softcleean.fastcleaner.R
import com.softcleean.fastcleaner.adapters.HintDecoration
import com.softcleean.fastcleaner.adapters.OptimizingRecyclerAdapter
import com.softcleean.fastcleaner.databinding.FragmentCoolingOptimizingBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class CoolingOptimizingFragment : Fragment(R.layout.fragment_cooling_optimizing) {

    private val binding: FragmentCoolingOptimizingBinding by viewBinding()

    private lateinit var adapter: OptimizingRecyclerAdapter

    private var isDoneOptimization = false

    private var listSize = 0
    private var listOptions = mutableListOf<String>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapter()
        setArrayOptionBoosting()
        startOptimization()
//        preloadInterstitial(BuildConfig.ADMOB_INTERSTITIAL5)
    }

    override fun onResume() {
        super.onResume()
        if (isDoneOptimization)
            findNavController().navigate(R.id.action_scanningFragment_to_homeFragment)
    }

    private fun startOptimization() {
        binding.ivBoosting.setImageDrawable(resources.getDrawable(R.drawable.ic_cooling_optimization))
        binding.tvProgressPercents.text = getString(R.string.value_percents, 0)
        lifecycleScope.launch {
            repeat(101) { percent ->
                delay(80)
                binding.tvProgressPercents.text = getString(R.string.value_percents, percent)
                binding.linearProgressIndicator.progress = percent
                withContext(Dispatchers.Main) {
                    updateList(progress = percent)
                }
                if (percent == 100) {
                    optimizationIsDone()
                    delay(500)
                    findNavController().navigate(R.id.action_coolingOptimizingFragment_to_coolingResultFragment)
//                    showInterstitial(
//                        onClosed = {
//                            findNavController().navigate(R.id.action_coolingOptimizingFragment_to_coolingResultFragment)
//                        }
//                    )
                }
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner, true
        ) {
        }
    }

    private fun optimizationIsDone() {
        isDoneOptimization = true
        with(binding) {
            tvProgressPercents.text = getString(R.string.ready)
            tvOptimizationTitle.isVisible = false
            recyclerView.isVisible = false
            ivBoosting.setImageDrawable(resources.getDrawable(R.drawable.ic_optimization_done))
        }
    }

    private fun setArrayOptionBoosting() {
        val list = resources.getStringArray(R.array.optimization).toList()
        adapter.submitList(list)
        listSize = list.size
        listOptions = list.toMutableList()
    }

    private fun initAdapter() {
        adapter = OptimizingRecyclerAdapter()
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.addItemDecoration(HintDecoration())
    }

    private fun updateList(
        valueToDelete: Int = 100 / listSize,
        progress: Int,
    ) {
        if (progress != 0 && progress % valueToDelete == 0 && listOptions.isNotEmpty()) {
            val list = mutableListOf<String>()
            listOptions.forEachIndexed { index, s ->
                if (index != 0) list.add(s)
            }
            listOptions = list
            adapter.submitList(list)
        }
    }

}