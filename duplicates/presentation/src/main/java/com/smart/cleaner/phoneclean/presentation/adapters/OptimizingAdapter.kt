package com.smart.cleaner.phoneclean.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.smart.cleaner.phoneclean.presentation.databinding.ItemOptimizingBinding

class OptimizingAdapter : ListAdapter<String, OptimizingAdapter.OptimizerItemViewHolder>(
    OptimizeFunItemDiffCallback()
) {

    class OptimizerItemViewHolder(
        private val binding: ItemOptimizingBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(option: String) {
            binding.tvOptimizingName.text = option
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


    class OptimizeFunItemDiffCallback : DiffUtil.ItemCallback<String>() {

        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean = oldItem == newItem

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean = oldItem == newItem

    }


}