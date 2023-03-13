package yin_kio.garbage_clean.presentation.views

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.recycler_adapter.recyclerAdapter
import jamycake.lifecycle_aware.lifecycleAware
import yin_kio.garbage_clean.presentation.R
import yin_kio.garbage_clean.presentation.databinding.FragmentGarbageCleanBinding
import yin_kio.garbage_clean.presentation.databinding.ListItemGarbageBinding
import yin_kio.garbage_clean.presentation.models.ScreenState
import yin_kio.garbage_clean.presentation.models.UiDeleteFromItem
import yin_kio.garbage_clean.presentation.view_model.ObservableScreenViewModel
import yin_kio.garbage_clean.presentation.view_model.parentViewModel

internal class GarbageCleanFragment : Fragment(R.layout.fragment_garbage_clean) {

    private val binding: FragmentGarbageCleanBinding by viewBinding()
    private val viewModel: ObservableScreenViewModel by lifecycleAware { parentViewModel() }

    private val adapter by lazy { adapter() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.recycler.adapter = adapter

        setupListeners()
        setupObserver()
    }

    private fun setupListeners() {
        binding.apply {
            selectAll.setOnClickListener { viewModel.switchSelectAll() }
            selectAllText.setOnClickListener { viewModel.switchSelectAll() }
            delete.setOnClickListener { viewModel.deleteIfCan() }
            back.setOnClickListener { viewModel.close() }
        }
    }

    private fun setupObserver() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.state.collect {
                updateUi(it)
            }
        }
    }

    private fun updateUi(it: ScreenState) {
        binding.apply {
            selectAll.isChecked = it.isAllSelected
            adapter.submitList(it.deleteFormItems)
            storageInfo.binding.progressBar.progress = it.fileSystemInfo.occupiedPercents
            progressPlate.isVisible = it.isInProgress
            canFree.text = it.canFreeVolume
            recycler.isVisible = it.deleteFormItems.isNotEmpty()
            deleteHasBeen.isVisible = it.deleteFormItems.isEmpty()
        }

        showFileSystemInfo(it)
        showButton(it)
    }

    private fun showButton(it: ScreenState) {
        binding.delete.apply {
            isVisible = it.buttonText.isNotEmpty()
            text = it.buttonText
            setBackgroundResource(it.buttonBgRes)
        }
    }

    private fun showFileSystemInfo(it: ScreenState) {
        binding.storageInfo.binding.apply {
            occupied.text = it.fileSystemInfo.occupied
            available.text = it.fileSystemInfo.available
            total.text = it.fileSystemInfo.total
        }
    }

    private fun adapter() = recyclerAdapter<UiDeleteFromItem, ListItemGarbageBinding>(
        onBind = { item, _ ->
            icon.setImageDrawable(ContextCompat.getDrawable(requireContext(), item.iconRes))
            name.text = item.name
            size.text = item.size
            checkbox.isChecked = item.isSelected
            root.setOnClickListener { viewModel.switchSelection(item.garbageType) }
            checkbox.setOnClickListener { viewModel.switchSelection(item.garbageType) }
        },
        areItemsTheSame = {old, new -> old.name == new.name}
    )


}