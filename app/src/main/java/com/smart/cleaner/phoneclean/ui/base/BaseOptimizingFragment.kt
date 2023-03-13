package com.smart.cleaner.phoneclean.ui.base

import android.app.Dialog
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.smart.cleaner.phoneclean.R
import com.smart.cleaner.phoneclean.adapters.HintDecoration
import com.smart.cleaner.phoneclean.adapters.OptimizingRecyclerAdapter
import com.smart.cleaner.phoneclean.ads.preloadInterstitial
import com.smart.cleaner.phoneclean.databinding.FragmentBaseOptimizingBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
abstract class BaseOptimizingFragment : DialogFragment(R.layout.fragment_base_optimizing) {

    private val binding: FragmentBaseOptimizingBinding by viewBinding()

    private lateinit var adapter: OptimizingRecyclerAdapter

    private var isDoneOptimization = false

    private var listSize = 0

    protected var listOptions: MutableList<String> = mutableListOf()
        set(list) {
            listSize = list.size
            field = list
            adapter.submitList(list)
        }

    abstract val keyInter: String
    abstract val nextScreenId: Int

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        setStyle(STYLE_NO_FRAME, R.style.Dialog)
        return super.onCreateDialog(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapter()
        setArrayOptimization()
        startOptimization()
        startOptimizationFun()
        dialog.apply { isCancelable = false }
        preloadInterstitial(keyInter)
    }

    abstract fun setArrayOptimization()

    abstract fun startOptimizationFun()

    abstract fun setFunName(): String

    private fun startOptimization() {
        binding.tvProgressPercents.text = getString(R.string.value_percents, 0)
        binding.tvOptimizationTitle.text = setFunName()
        binding.optimizationRunning.isVisible = true
        binding.optimizationDone.isVisible = false
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
                    delay(700)
                    findNavController().navigate(nextScreenId)
                }
            }
        }
    }

    private fun optimizationIsDone() {
        isDoneOptimization = true
        with(binding) {
            tvProgressPercents.text = getString(R.string.ready)
            recyclerView.isVisible = false
            optimizationRunning.isVisible = false
            optimizationDone.isVisible = true
        }
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

    override fun onResume() {
        super.onResume()
        if (isDoneOptimization)
            findNavController().navigate(R.id.action_batteryOptimizingFragment_to_batteryResultFragment)
    }
}