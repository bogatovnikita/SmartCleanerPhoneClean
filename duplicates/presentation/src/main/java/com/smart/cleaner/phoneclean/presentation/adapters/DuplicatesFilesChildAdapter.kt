package com.smart.cleaner.phoneclean.presentation.adapters

import android.text.format.Formatter
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.smart.cleaner.phoneclean.presentation.R
import com.smart.cleaner.phoneclean.presentation.adapters.listeners.OnFileChangeSelectListener
import com.smart.cleaner.phoneclean.presentation.adapters.models.ChildFileItem
import com.smart.cleaner.phoneclean.presentation.databinding.ItemChildFileBinding

class DuplicatesFilesChildAdapter(private val listener: OnFileChangeSelectListener) :
    RecyclerView.Adapter<DuplicatesFilesChildAdapter.ChildFileViewHolder>() {

    private var items: List<ChildFileItem> = emptyList()

    fun submitList(list: List<ChildFileItem>) {
        items = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChildFileViewHolder {
        val binding =
            ItemChildFileBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ChildFileViewHolder(listener, binding)
    }

    override fun onBindViewHolder(holder: ChildFileViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = items.size

    class ChildFileViewHolder(
        private val listener: OnFileChangeSelectListener,
        private val binding: ItemChildFileBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ChildFileItem) {
            with(binding) {
                tvName.text = item.fileName
                tvSizeAndPath.text = root.context.getString(
                    R.string.duplicates_size_and_path,
                    Formatter.formatFileSize(root.context, item.size),
                    item.filePath
                )
                btnSwitchOffFile.isVisible = item.isSelected
                btnSwitchOnFile.isVisible = !item.isSelected
                btnSwitchOnFile.setOnClickListener {
                    listener.selectFile(item, true)
                }
                btnSwitchOffFile.setOnClickListener {
                    listener.selectFile(item, false)
                }
                root.setOnClickListener {
                    listener.selectFile(item, !item.isSelected)
                }
            }
        }
    }
}
