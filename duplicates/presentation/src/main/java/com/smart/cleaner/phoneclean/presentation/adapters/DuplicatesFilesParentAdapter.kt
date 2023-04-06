package com.smart.cleaner.phoneclean.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.smart.cleaner.phoneclean.presentation.R
import com.smart.cleaner.phoneclean.presentation.adapters.listeners.OnFileChangeSelectListener
import com.smart.cleaner.phoneclean.presentation.adapters.models.ParentFileItem
import com.smart.cleaner.phoneclean.presentation.databinding.ItemParentFilesBinding

class DuplicatesFilesParentAdapter(private val listener: OnFileChangeSelectListener) :
    ListAdapter<ParentFileItem, DuplicatesFilesParentAdapter.ParentFileViewHolder>(
        ParentFileItemDiffCallback()
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ParentFileViewHolder {
        val binding =
            ItemParentFilesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ParentFileViewHolder(listener, binding)
    }

    override fun onBindViewHolder(holder: ParentFileViewHolder, position: Int) {
        val fileGroup = getItem(position)
        holder.bind(fileGroup, position, currentList.size)
    }

    inner class ParentFileViewHolder(
        private val listener: OnFileChangeSelectListener,
        private val binding: ItemParentFilesBinding,
    ) :
        RecyclerView.ViewHolder(binding.root) {

        private val adapter = DuplicatesFilesChildAdapter(listener)

        init {
            initAdapter()
        }

        fun bind(state: ParentFileItem, position: Int, generalListSize: Int) {
            with(binding) {
                tvNumberDuplicates.text = binding.root.context.getString(
                    R.string.duplicates_number_of_duplicate,
                    state.count
                )
                binding.btnSwitchOffAll.isVisible = state.isAllSelected
                binding.btnSwitchOnAll.isVisible = !state.isAllSelected
                separationLine.isVisible = position == generalListSize
                btnSwitchOffAll.setOnClickListener {
                    listener.selectAll(state, false)
                }
                btnSwitchOnAll.setOnClickListener {
                    listener.selectAll(state, true)
                }
                adapter.submitList(state.files)
                separationLine.isVisible = position != currentList.lastIndex
            }
        }

        private fun initAdapter() {
            binding.childRecyclerView.adapter = adapter
            val layoutManager =
                LinearLayoutManager(binding.root.context, LinearLayoutManager.VERTICAL, false)
            binding.childRecyclerView.layoutManager = layoutManager
        }

    }

    class ParentFileItemDiffCallback : DiffUtil.ItemCallback<ParentFileItem>() {

        override fun areItemsTheSame(oldItem: ParentFileItem, newItem: ParentFileItem) = newItem.count == oldItem.count

        override fun areContentsTheSame(oldItem: ParentFileItem, newItem: ParentFileItem) = oldItem == newItem

    }

}