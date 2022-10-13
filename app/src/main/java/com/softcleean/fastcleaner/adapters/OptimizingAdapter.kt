package com.softcleean.fastcleaner.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.softcleean.fastcleaner.databinding.ItemOptimizingBinding


class OptimizeFunItemDiffCallback : DiffUtil.ItemCallback<String>() {

    override fun areItemsTheSame(oldItem: String, newItem: String): Boolean = oldItem == newItem

    override fun areContentsTheSame(oldItem: String, newItem: String): Boolean = oldItem == newItem

}

class OptimizingRecyclerAdapter : ListAdapter<String, OptimizingRecyclerAdapter.OptimizerItemViewHolder>(OptimizeFunItemDiffCallback()) {

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

}
