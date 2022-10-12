package com.entertainment.event.ssearch.ar155.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.entertainment.event.ssearch.ar155.databinding.ItemHomeFunBinding


class HomeFunItemDiffCallback : DiffUtil.ItemCallback<ItemHomeFun>() {

    override fun areItemsTheSame(oldItem: ItemHomeFun, newItem: ItemHomeFun): Boolean =
        oldItem.funName == newItem.funName

    override fun areContentsTheSame(oldItem: ItemHomeFun, newItem: ItemHomeFun): Boolean = oldItem == newItem

}
class HomeFunAdapter(private val listener: ClickOnFunListener): ListAdapter<ItemHomeFun, HomeFunAdapter.HomeFunItemViewHolder> (HomeFunItemDiffCallback()) {

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

    override fun onBindViewHolder(holder: HomeFunItemViewHolder, position: Int) = holder.bind(getItem(position))

    interface ClickOnFunListener {
        fun onFunClick(item: ItemHomeFun)
    }
}

data class ItemHomeFun(
    val isOptimized: Boolean = false,
    val funName: Int = 0,
    val funDangerDescription: Int = 0,
    val icon: Int = 0,
    val type: String = BOOST,
    val btnText: Int = 0,
) {
    companion object {
        const val CLEAN = "CLEAN"
        const val BOOST = "BOOST"
        const val BATTERY = "BATTERY"
        const val COOLING = "COOLING"
    }
}