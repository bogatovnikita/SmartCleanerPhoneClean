package com.smart.cleaner.phoneclean.presentation.ui.optimizing

import android.app.Dialog
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.ads.preloadAd
import com.example.ads.showInter
import com.smart.cleaner.phoneclean.presentation.R
import com.smart.cleaner.phoneclean.presentation.adapters.models.ParentImageItem
import com.smart.cleaner.phoneclean.presentation.databinding.FragmentOptimizingBinding
import com.smart.cleaner.phoneclean.presentation.ui.duplicate_images.DuplicateImagesViewModel
import com.smart.cleaner.phoneclean.presentation.ui.duplicate_images.ImagesStateScreen
import com.smart.cleaner.phoneclean.ui_core.adapters.HintDecoration
import com.smart.cleaner.phoneclean.ui_core.adapters.OptimizingAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class OptimizingFragment : DialogFragment(R.layout.fragment_optimizing) {

    private val binding: FragmentOptimizingBinding by viewBinding()

    private val imagesViewModel: DuplicateImagesViewModel by activityViewModels()

    private lateinit var adapter: OptimizingAdapter

    private var isDoneOptimization = false

    private var nextScreenId = 0

    private var listSize = 0

    private var listOptions: MutableList<String> = mutableListOf()
        set(list) {
            listSize = list.size
            field = list
            adapter.submitList(list)
        }

    private val delayTime = 8000L

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        setStyle(STYLE_NO_FRAME, general.R.style.Dialog)
        return super.onCreateDialog(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        nextScreenId =
            if (imagesViewModel.screenState.value.event is ImagesStateScreen.ImageEvent.ConfirmedImageDeletion)
                R.id.action_to_requestStoragePermDialog else R.id.action_to_requestStoragePermDialog

        initAdapter()
        startOptimization()

        dialog.apply { isCancelable = false }
        preloadAd()

    }

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
        binding.tvProgressPercents.text = getString(R.string.value_percents, 0)
    }

    private fun renderInProgressOptimization(percent: Int) {
        binding.tvProgressPercents.text = getString(R.string.value_percents, percent)
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
        listOptions = toListString(imagesViewModel.screenState.value.duplicates).toMutableList()
    }

    private fun toListString(duplicates: List<ParentImageItem>): List<String> {
        val newList = mutableListOf<String>()
        duplicates.forEach { item ->
            item.images.forEach { image ->
                if (image.isSelected) {
                    newList.add(image.imagePath)
                }
            }
        }
        return newList
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
            findNavController().navigate(nextScreenId)
    }

}