package com.smart.cleaner.phoneclean.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.smart.cleaner.phoneclean.presentation.R
import com.smart.cleaner.phoneclean.presentation.adapters.listeners.OnImageChangeSelectListener
import com.smart.cleaner.phoneclean.presentation.adapters.models.ParentImageItem
import com.smart.cleaner.phoneclean.presentation.databinding.ItemDuplicatesImageBinding

class DuplicatesImagesParentAdapter(private val listener: OnImageChangeSelectListener) :
    RecyclerView.Adapter<DuplicatesImagesParentAdapter.ParentViewHolder>() {

    private val items: MutableList<ParentImageItem> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ParentViewHolder {
        val binding =
            ItemDuplicatesImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ParentViewHolder(listener, binding)
    }

    override fun onBindViewHolder(holder: ParentViewHolder, position: Int) {
        val imageGroup = items[position]
        holder.bind(imageGroup, position)
    }

    override fun getItemCount() = items.size

    fun setItems(newItems: List<ParentImageItem>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    inner class ParentViewHolder(
        private val listener: OnImageChangeSelectListener,
        private val binding: ItemDuplicatesImageBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        private val adapter = DuplicatesImagesChildAdapter(listener)

        init {
            initAdapter()
        }

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
                adapter.setItems(state.images)
                separationLine.isVisible = position != items.lastIndex
            }
        }

        private fun initAdapter() {
            binding.recyclerViewImageDuplicates.adapter = adapter
            val layoutManager =
                LinearLayoutManager(binding.root.context, LinearLayoutManager.HORIZONTAL, false)
            binding.recyclerViewImageDuplicates.layoutManager = layoutManager
        }

    }
}
