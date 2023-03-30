package com.smart.cleaner.phoneclean.ui_core.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.smart.cleaner.phoneclean.ui_core.adapters.models.FunResult
import com.smart.cleaner.phoneclean.ui_core.databinding.ItemFunResultBinding


class ResultFunAdapter(private val listener: ClickOnFunResultListener) :
    ListAdapter<FunResult, ResultFunAdapter.FunResultItemViewHolder>(ResultFunItemDiffCallback()) {

    class FunResultItemViewHolder(
        private val binding: ItemFunResultBinding,
        private val listener: ClickOnFunResultListener,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: FunResult) {
            if (item.isOptimized) {
                binding.tvFunDone.isVisible = true
                binding.tvFunDangerDescription.isVisible = false
            } else {
                binding.tvFunDone.isVisible = false
                binding.tvFunDangerDescription.isVisible = true
                binding.tvFunDangerDescription.setText(item.funDangerDescription)
            }
            binding.tvFunName.setText(item.funName)
            binding.ivFun.setImageDrawable(binding.root.resources.getDrawable(item.icon))
            binding.root.setOnClickListener {
                listener.onFunClick(item)
            }
        }

    }

    interface ClickOnFunResultListener {
        fun onFunClick(item: FunResult)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FunResultItemViewHolder {
        val binding =
            ItemFunResultBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FunResultItemViewHolder(binding, listener)
    }

    override fun onBindViewHolder(holder: FunResultItemViewHolder, position: Int) =
        holder.bind(getItem(position))


    class ResultFunItemDiffCallback : DiffUtil.ItemCallback<FunResult>() {

        override fun areItemsTheSame(oldItem: FunResult, newItem: FunResult): Boolean =
            oldItem.funName == newItem.funName

        override fun areContentsTheSame(oldItem: FunResult, newItem: FunResult): Boolean =
            oldItem == newItem

    }
}