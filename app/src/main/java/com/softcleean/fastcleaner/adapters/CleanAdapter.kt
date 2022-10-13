package com.softcleean.fastcleaner.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.softcleean.fastcleaner.R
import com.softcleean.fastcleaner.databinding.ItemCleanBinding

class CleanItemDiffCallback : DiffUtil.ItemCallback<ItemGarbage>() {

    override fun areItemsTheSame(oldItem: ItemGarbage, newItem: ItemGarbage): Boolean =
        oldItem.junkType == newItem.junkType

    override fun areContentsTheSame(oldItem: ItemGarbage, newItem: ItemGarbage): Boolean = oldItem == newItem

}

class CleanAdapter : ListAdapter<ItemGarbage, CleanAdapter.CleanItemViewHolder>(CleanItemDiffCallback()) {

    class CleanItemViewHolder(
        private val binding: ItemCleanBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ItemGarbage) {
            if (item.isCleaned) {
                binding.tvJunkSize.isVisible = false
                binding.tvJunkCleared.isVisible = true
            } else {
                binding.tvJunkSize.isVisible = true
                binding.tvJunkCleared.isVisible = false
                binding.tvJunkSize.text = binding.root.context.getString(R.string.mb, item.junkSize)
            }
            binding.tvTypeJunk.text = item.junkType
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CleanItemViewHolder {
        val binding =
            ItemCleanBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CleanItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CleanItemViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

data class ItemGarbage(
    val isCleaned: Boolean = false,
    val junkType: String = "Кэш",
    val junkSize: Int = 0,
)