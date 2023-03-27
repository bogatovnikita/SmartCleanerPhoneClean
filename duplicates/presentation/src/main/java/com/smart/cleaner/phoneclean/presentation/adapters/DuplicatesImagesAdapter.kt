package com.smart.cleaner.phoneclean.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.smart.cleaner.phoneclean.presentation.R
import com.smart.cleaner.phoneclean.presentation.adapters.listeners.OnChangeSelectListener
import com.smart.cleaner.phoneclean.presentation.adapters.models.ParentImageItem
import com.smart.cleaner.phoneclean.presentation.databinding.ItemDuplicatesImageBinding

class DuplicatesImagesAdapter(private val listener: OnChangeSelectListener) :
    ListAdapter<ParentImageItem, DuplicatesImagesAdapter.ParentViewHolder>(
        ParentImageItemDiffCallback()
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ParentViewHolder {
        val binding =
            ItemDuplicatesImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ParentViewHolder(listener, binding)
    }

    override fun onBindViewHolder(holder: ParentViewHolder, position: Int) {
        val imageGroup = getItem(position)
        holder.bind(imageGroup, position)
    }

    class ParentViewHolder(
        private val listener: OnChangeSelectListener,
        private val binding: ItemDuplicatesImageBinding,
    ) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(state: ParentImageItem, position: Int) {
            with(binding) {
                tvNumberDuplicates.text = binding.root.context.getString(
                    R.string.duplicates_number_of_duplicate,
                    state.count
                )
                binding.btnSwitchOffAll.isVisible = state.isAllSelected
                binding.btnSwitchOnAll.isVisible = !state.isAllSelected
                btnSwitchOffAll.setOnClickListener {
                    listener.selectAll(state, false)
                }
                btnSwitchOnAll.setOnClickListener {
                    listener.selectAll(state, true)
                }
            }
        }

    }

    class ParentImageItemDiffCallback : DiffUtil.ItemCallback<ParentImageItem>() {

        override fun areItemsTheSame(oldItem: ParentImageItem, newItem: ParentImageItem) =
            oldItem.images == newItem.images

        override fun areContentsTheSame(
            oldItem: ParentImageItem,
            newItem: ParentImageItem
        ) = oldItem == newItem


    }

}