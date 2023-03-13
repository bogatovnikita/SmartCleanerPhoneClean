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
import com.smart.cleaner.phoneclean.ads.showInterstitial
import com.smart.cleaner.phoneclean.databinding.FragmentBaseOptimizingBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

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

    private val delayTime = 8000L

    abstract val keyInter: String
    abstract val nextScreenId: Int

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        setStyle(STYLE_NO_FRAME, R.style.Dialog)
        return super.onCreateDialog(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapter()
        getArrayOptimization()
        startOptimization()
        startOptimizationFun()
        dialog.apply { isCancelable = false }
        preloadInterstitial(keyInter)
    }

    abstract fun getArrayOptimization()

    abstract fun startOptimizationFun()

    abstract fun getFunName(): String

    private fun startOptimization() {
        initStartOptimization()
        lifecycleScope.launch(Dispatchers.Main) {
            val maxPercent = 101
            repeat(maxPercent) { percent ->
                delay(delayTime/maxPercent)
                renderInProgressOptimization(percent)
                when(percent) {
                    100 -> optimizationIsDone()
                }
            }
        }
    }

    private fun initStartOptimization() {
        binding.tvProgressPercents.text = getString(R.string.value_percents, 0)
        binding.tvOptimizationTitle.text = getFunName()
    }

    private fun renderInProgressOptimization(percent: Int) {
        binding.tvProgressPercents.text = getString(R.string.value_percents, percent)
        updateList(progress = percent)
    }


    private fun optimizationIsDone() {
        isDoneOptimization = true
        with(binding) {
            tvProgressPercents.text = getString(R.string.ready)
            recyclerView.isVisible = false
        }
        navigateNext()
    }

    private fun navigateNext() {
        lifecycleScope.launch {
            delay(700)
            showInterstitial(
                onClosed = { findNavController().navigate(nextScreenId) }
            )
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