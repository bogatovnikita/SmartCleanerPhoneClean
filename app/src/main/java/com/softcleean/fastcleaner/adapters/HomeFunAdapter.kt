package com.softcleean.fastcleaner.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.softcleean.fastcleaner.databinding.ItemHomeFunBinding
import com.softcleean.fastcleaner.utils.OptimizingType


class HomeFunItemDiffCallback : DiffUtil.ItemCallback<ItemHomeFun>() {

    override fun areItemsTheSame(oldItem: ItemHomeFun, newItem: ItemHomeFun): Boolean =
        oldItem.funName == newItem.funName

    override fun areContentsTheSame(oldItem: ItemHomeFun, newItem: ItemHomeFun): Boolean =
        oldItem == newItem

}

class HomeFunAdapter(private val listener: ClickOnFunListener) :
    ListAdapter<ItemHomeFun, HomeFunAdapter.HomeFunItemViewHolder>(HomeFunItemDiffCallback()) {

    class HomeFunItemViewHolder(
        private val binding: ItemHomeFunBinding,
        private val listener: ClickOnFunListener,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ItemHomeFun) {
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
            binding.btnOpenFun.setText(item.btnText)
            binding.btnOpenFun.setOnClickListener {
                listener.onFunClick(item)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeFunItemViewHolder {
        val binding =
            ItemHomeFunBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HomeFunItemViewHolder(binding, listener)
    }

    override fun onBindViewHolder(holder: HomeFunItemViewHolder, position: Int) =
        holder.bind(getItem(position))

    interface ClickOnFunListener {
        fun onFunClick(item: ItemHomeFun)
    }
}

data class ItemHomeFun(
    val isOptimized: Boolean = false,
    val funName: Int = 0,
    val funDangerDescription: Int = 0,
    val icon: Int = 0,
    val type: OptimizingType = OptimizingType.Boost,
    val btnText: Int = 0,
)