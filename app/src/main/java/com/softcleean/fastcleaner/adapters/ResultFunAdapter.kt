package com.softcleean.fastcleaner.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.softcleean.fastcleaner.databinding.ItemFunResultBinding
import com.softcleean.fastcleaner.ui.result.FunResult

class ResultFunItemDiffCallback : DiffUtil.ItemCallback<FunResult>() {

    override fun areItemsTheSame(oldItem: FunResult, newItem: FunResult): Boolean =
        oldItem.funName == newItem.funName

    override fun areContentsTheSame(oldItem: FunResult, newItem: FunResult): Boolean =
        oldItem == newItem

}

class ResultFunAdapter(private val listener: ClickOnFunResultListener) :
    ListAdapter<FunResult, ResultFunAdapter.FunResultItemViewHolder>(ResultFunItemDiffCallback()) {

    class FunResultItemViewHolder(
        private val binding: ItemFunResultBinding,
        private val listener: ClickOnFunResultListener,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: FunResult) {
            if (item.isOptimized) {
                binding.groupDangerExist.isVisible = false
                binding.groupNoDanger.isVisible = true
            } else {
                binding.groupDangerExist.isVisible = true
                binding.groupNoDanger.isVisible = false
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

}