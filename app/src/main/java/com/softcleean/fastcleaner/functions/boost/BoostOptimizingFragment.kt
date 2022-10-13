package com.softcleean.fastcleaner.functions.boost

import android.os.Bundle
import android.view.View
import androidx.activity.addCallback
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.softcleean.fastcleaner.BuildConfig
import com.softcleean.fastcleaner.R
import com.softcleean.fastcleaner.databinding.FragmentBoostOptimizingBinding
import com.softcleean.fastcleaner.adapters.HintDecoration
import com.softcleean.fastcleaner.adapters.OptimizingRecyclerAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import preloadInterstitial
import showInterstitial

class BoostOptimizingFragment : Fragment(R.layout.fragment_boost_optimizing) {

    private val binding: FragmentBoostOptimizingBinding by viewBinding()

    private lateinit var adapter: OptimizingRecyclerAdapter

    private var listSize = 0
    private var listOptions = mutableListOf<String>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapter()
        setArrayOptionBoosting()
        startOptimization()
        preloadInterstitial(BuildConfig.ADMOB_INTERSTITIAL3)
    }

    private fun startOptimization() {
        binding.ivBoosting.setImageDrawable(resources.getDrawable(R.drawable.ic_optimization_boost))
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
                    showInterstitial(
                        onClosed = {
                            findNavController().navigate(R.id.action_boostOptimizingFragment_to_boostResultFragment)
                        }
                    )
                }
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner, true
        ) {
        }
    }

    private fun optimizationIsDone() {
        with(binding) {
            tvProgressPercents.text = getString(R.string.ready)
            recyclerView.isVisible = false
            tvOptimizationTitle.isVisible = false
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