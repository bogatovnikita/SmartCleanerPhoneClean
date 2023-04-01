package com.smart.cleaner.phoneclean.ui_core.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.smart.cleaner.phoneclean.ui_core.adapters.models.BoostOptimizingItem
import com.smart.cleaner.phoneclean.ui_core.adapters.models.GeneralOptimizingItem
import com.smart.cleaner.phoneclean.ui_core.adapters.models.OptimizingItem
import com.smart.cleaner.phoneclean.ui_core.databinding.ItemOptimizingBinding

class OptimizingAdapter : ListAdapter<OptimizingItem, OptimizingAdapter.OptimizerItemViewHolder>(
    OptimizeFunItemDiffCallback()
) {

    class OptimizerItemViewHolder(
        private val binding: ItemOptimizingBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(option: OptimizingItem) {
            binding.tvOptimizingName.text = option.name
            if (option is GeneralOptimizingItem) {
                binding.ivIcon.isVisible = false
            }
            if (option is BoostOptimizingItem) {
                binding.ivIcon.isVisible = true
                binding.ivIcon.setImageResource(option.icon)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OptimizerItemViewHolder {
        val binding =
            ItemOptimizingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OptimizerItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OptimizerItemViewHolder, position: Int) {
        holder.bind(getItem(position))
    }


    class OptimizeFunItemDiffCallback : DiffUtil.ItemCallback<OptimizingItem>() {

        override fun areItemsTheSame(oldItem: OptimizingItem, newItem: OptimizingItem) =
            oldItem.name == newItem.name

        override fun areContentsTheSame(oldItem: OptimizingItem, newItem: OptimizingItem) =
            oldItem.name == newItem.name

    }


}