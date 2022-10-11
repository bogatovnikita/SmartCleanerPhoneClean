package com.entertainment.event.ssearch.ar155.functions.clean

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.entertainment.event.ssearch.ar155.R
import com.entertainment.event.ssearch.ar155.adapters.HintDecoration
import com.entertainment.event.ssearch.ar155.adapters.OptimizingRecyclerAdapter
import com.entertainment.event.ssearch.ar155.databinding.FragmentBoostOptimizingBinding
import com.entertainment.event.ssearch.ar155.databinding.FragmentCleanOptimizingBinding
import com.entertainment.event.ssearch.domain.clean.CleanUseCase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.random.Random

@AndroidEntryPoint
class CleanOptimizingFragment : Fragment(R.layout.fragment_clean_optimizing) {

    private val binding: FragmentCleanOptimizingBinding by viewBinding()

    private lateinit var adapter: OptimizingRecyclerAdapter

    @Inject
    lateinit var cleanUseCase: CleanUseCase

    private var listSize = 0
    private var listOptions = mutableListOf<String>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapter()
        startOptimization()
    }

    private fun startOptimization() {
        setArrayOptionBoosting()
        binding.ivBoosting.setImageDrawable(resources.getDrawable(R.drawable.ic_optimization_clean))
        lifecycleScope.launch {
            repeat(101) { percent ->
                delay(160)
                binding.tvProgressPercents.text = getString(R.string.value_percents, percent)
                binding.linearProgressIndicator.progress = percent
                withContext(Dispatchers.Main) {
                    updateList(progress = percent)
                }
                if (percent == 100) {
                    optimizationIsDone()
                    delay(500)
                    findNavController().popBackStack()
                }
            }
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
        val randomType = Random.nextInt(1, 3)
        val list = cleanUseCase.getFolders().toList().filter { path ->
            when (randomType) {
                1 -> path.length % 2 == 0
                2 -> path.length % 2 != 0
                3 -> path.length % 4 == 0
                else -> path.length % 3 == 0
            }
        }
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