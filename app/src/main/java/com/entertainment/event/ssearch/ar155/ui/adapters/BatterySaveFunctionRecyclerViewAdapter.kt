package com.entertainment.event.ssearch.ar155.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.entertainment.event.ssearch.ar155.databinding.ItemBatterySaveFunBinding

class FunItemDiffCallback : DiffUtil.ItemCallback<String>() {
    override fun areItemsTheSame(oldItem: String, newItem: String): Boolean = oldItem == newItem

    override fun areContentsTheSame(oldItem: String, newItem: String): Boolean = oldItem == newItem

}

class BatterySaveFunctionRecyclerViewAdapter :
    ListAdapter<String, BatterySaveFunctionRecyclerViewAdapter.FunSaveItemViewHolder>(FunItemDiffCallback()) {

    class FunSaveItemViewHolder(private val binding: ItemBatterySaveFunBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun binTo(function: String) {
            binding.function.text = function
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FunSaveItemViewHolder {
        val binding = ItemBatterySaveFunBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return  FunSaveItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FunSaveItemViewHolder, position: Int) {
        holder.binTo(getItem(position))
    }
}