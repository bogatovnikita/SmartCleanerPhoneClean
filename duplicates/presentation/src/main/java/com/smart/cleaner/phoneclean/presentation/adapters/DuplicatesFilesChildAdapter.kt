package com.smart.cleaner.phoneclean.presentation.adapters

import android.text.format.Formatter
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.smart.cleaner.phoneclean.presentation.R
import com.smart.cleaner.phoneclean.presentation.adapters.listeners.OnFileChangeSelectListener
import com.smart.cleaner.phoneclean.presentation.adapters.models.ChildFileItem
import com.smart.cleaner.phoneclean.presentation.databinding.ItemChildFileBinding

class DuplicatesFilesChildAdapter(private val listener: OnFileChangeSelectListener) :
    ListAdapter<ChildFileItem, DuplicatesFilesChildAdapter.ChildFileViewHolder>(
        ChildFileItemDiffCallback()
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChildFileViewHolder {
        val binding =
            ItemChildFileBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ChildFileViewHolder(listener, binding)
    }

    override fun onBindViewHolder(holder: ChildFileViewHolder, position: Int) {
        val image = getItem(position)
        holder.bind(image)
    }

    class ChildFileViewHolder(
        private val listener: OnFileChangeSelectListener,
        private val binding: ItemChildFileBinding,
    ) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(state: ChildFileItem) {
            with(binding) {
                tvName.text = state.fileName
                tvSizeAndPath.text = binding.root.context.getString(
                    R.string.duplicates_size_and_path,
                    Formatter.formatFileSize(binding.root.context, state.size),
                    state.filePath
                )
                btnSwitchOffFile.isVisible = state.isSelected
                btnSwitchOnFile.isVisible = !state.isSelected
                btnSwitchOnFile.setOnClickListener {
                    listener.selectFile(state, true)
                }
                btnSwitchOffFile.setOnClickListener {
                    listener.selectFile(state, false)
                }
                binding.root.setOnClickListener {
                    listener.selectFile(state, !state.isSelected)
                }
            }
        }

    }

    class ChildFileItemDiffCallback : DiffUtil.ItemCallback<ChildFileItem>() {

        override fun areItemsTheSame(oldItem: ChildFileItem, newItem: ChildFileItem) =
            newItem.isSelected == oldItem.isSelected

        override fun areContentsTheSame(oldItem: ChildFileItem, newItem: ChildFileItem) =
            newItem == oldItem

    }
}