package yin_kio.garbage_clean.presentation.garbage_list.adapter

import android.text.format.Formatter.formatFileSize
import android.view.LayoutInflater
import android.view.ViewGroup
import pokercc.android.expandablerecyclerview.ExpandableAdapter
import yin_kio.garbage_clean.domain.services.garbage_files.GarbageType
import yin_kio.garbage_clean.domain.ui_out.Checkable
import yin_kio.garbage_clean.presentation.R
import yin_kio.garbage_clean.presentation.databinding.ListItemGarbageBinding
import java.io.File

class FileViewHolder private constructor(
    private val binding: ListItemGarbageBinding,
    private val onClick: (GarbageType, File, Checkable) -> Unit,
    private val onUpdate: (GarbageType, File, Checkable) -> Unit
) : ExpandableAdapter.ViewHolder(binding.root) {

    private val checkboxWrapper = CheckboxWrapper(binding.checkbox)

    fun bind(garbageType: GarbageType, file: File){
        onUpdate(garbageType, file, checkboxWrapper)

        binding.size.text = formatFileSize(binding.root.context, file.length())
        binding.name.text = file.name
        binding.icon.setImageResource(iconRes(file))

        binding.checkbox.setOnClickListener {
            onClick(garbageType, file, checkboxWrapper)
        }
    }

    private fun iconRes(file: File) : Int {
        return if (file.isDirectory) {
            R.drawable.ic_folder
        } else {
            R.drawable.ic_document
        }
    }

    companion object {

        fun from(
            parent: ViewGroup,
            onClick: (GarbageType, File, Checkable) -> Unit,
            onUpdate: (GarbageType, File, Checkable) -> Unit
        ) : FileViewHolder {
            val binding = ListItemGarbageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return FileViewHolder(
                binding = binding,
                onUpdate = onUpdate,
                onClick = onClick
            )
        }

    }

}