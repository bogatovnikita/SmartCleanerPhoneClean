package com.smart.cleaner.phoneclean.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.smart.cleaner.phoneclean.presentation.adapters.listeners.OnImageChangeSelectListener
import com.smart.cleaner.phoneclean.presentation.adapters.models.ChildImageItem
import com.smart.cleaner.phoneclean.presentation.databinding.ItemImageBinding

class DuplicatesImagesChildAdapter(private val listener: OnImageChangeSelectListener) :
    RecyclerView.Adapter<DuplicatesImagesChildAdapter.ChildViewHolder>() {

    private val items: MutableList<ChildImageItem> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChildViewHolder {
        val binding =
            ItemImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ChildViewHolder(listener, binding)
    }

    override fun onBindViewHolder(holder: ChildViewHolder, position: Int) {
        val image = items[position]
        holder.bind(image)
    }

    override fun getItemCount() = items.size

    fun setItems(newItems: List<ChildImageItem>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    inner class ChildViewHolder(
        private val listener: OnImageChangeSelectListener,
        private val binding: ItemImageBinding,
    ) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(state: ChildImageItem) {
            with(binding) {
                Glide.with(root).load(state.imagePath).into(image)
                btnSwitchOff.isVisible = state.isSelected
                btnSwitchOn.isVisible = !state.isSelected
                btnSwitchOn.setOnClickListener {
                    listener.selectImage(state, true)
                }
                btnSwitchOff.setOnClickListener {
                    listener.selectImage(state, false)
                }
                image.setOnClickListener {
                    listener.selectImage(state, !state.isSelected)
                }
            }
        }
    }
}

