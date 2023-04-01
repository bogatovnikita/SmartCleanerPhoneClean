package com.smart.cleaner.phoneclean.ui_core.adapters.base

import android.app.Dialog
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.ads.preloadAd
import com.example.ads.showInter
import com.smart.cleaner.phoneclean.ui_core.R
import com.smart.cleaner.phoneclean.ui_core.adapters.HintDecoration
import com.smart.cleaner.phoneclean.ui_core.adapters.OptimizingAdapter
import com.smart.cleaner.phoneclean.ui_core.adapters.models.OptimizingItem
import com.smart.cleaner.phoneclean.ui_core.databinding.FragmentBaseOptimizingBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

abstract class BaseOptimizingFragment : DialogFragment(R.layout.fragment_base_optimizing) {

    private val binding: FragmentBaseOptimizingBinding by viewBinding()

    private lateinit var adapter: OptimizingAdapter

    private var isDoneOptimization = false

    private var listSize = 0

    protected var listOptions: MutableList<OptimizingItem> = mutableListOf()
        set(list) {
            if (list.size > field.size)
                listSize = list.size
            field = list
            adapter.submitList(list)
        }

    private val delayTime = 8000L

    abstract val nextScreenId: Int

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        setStyle(STYLE_NO_FRAME, general.R.style.Dialog)
        return super.onCreateDialog(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapter()
        getArrayOptimization()
        startOptimization()
        startOptimizationFun()
        dialog.apply { isCancelable = false }
        preloadAd()
    }

    abstract fun getArrayOptimization()

    abstract fun startOptimizationFun()

    abstract fun getFunName(): String

    private fun startOptimization() {
        initStartOptimization()
        lifecycleScope.launch(Dispatchers.Main) {
            val maxPercent = 101
            repeat(maxPercent) { percent ->
                delay(delayTime / maxPercent)
                renderInProgressOptimization(percent)
                when (percent) {
                    100 -> optimizationIsDone()
                }
            }
        }
    }

    private fun initStartOptimization() {
        binding.tvProgressPercents.text = getString(general.R.string.value_percents, 0)
        binding.tvOptimizationTitle.text = getFunName()
    }

    private fun renderInProgressOptimization(percent: Int) {
        binding.tvProgressPercents.text = getString(general.R.string.value_percents, percent)
        updateList(progress = percent)
    }


    private fun optimizationIsDone() {
        isDoneOptimization = true
        binding.recyclerView.isVisible = false
        navigateNext()
    }

    private fun navigateNext() {
        lifecycleScope.launch {
            delay(700)
            showInter(
                onClosed = { findNavController().navigate(nextScreenId) }
            )
        }
    }

    private fun initAdapter() {
        adapter = OptimizingAdapter()
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.addItemDecoration(HintDecoration())
    }

    private fun updateList(
        progress: Int,
    ) {
        val valueToDelete = if (listSize != 0) 100 / listSize else 99
        if (progress != 0 && progress % valueToDelete == 0 && listOptions.isNotEmpty()) {
            val list = mutableListOf<OptimizingItem>()
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
            findNavController().navigate(nextScreenId)
    }

}