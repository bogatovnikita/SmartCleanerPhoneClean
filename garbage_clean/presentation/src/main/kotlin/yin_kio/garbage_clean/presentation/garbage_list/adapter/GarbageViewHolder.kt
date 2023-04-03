package yin_kio.garbage_clean.presentation.garbage_list.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import pokercc.android.expandablerecyclerview.ExpandableAdapter
import yin_kio.garbage_clean.domain.services.garbage_files.GarbageType
import yin_kio.garbage_clean.domain.ui_out.Checkable
import yin_kio.garbage_clean.presentation.garbage_list.adapter.models.GarbageGroup
import yin_kio.garbage_clean.presentation.databinding.HeaderGarbageBinding

class GarbageViewHolder private constructor(
    private val binding: HeaderGarbageBinding,
    private val onUpdate: (GarbageType, Checkable) -> Unit,
    private val onClick: (GarbageType, Checkable) -> Unit
) : ExpandableAdapter.ViewHolder(binding.root) {

    private val checkboxWrapper = CheckboxWrapper(binding.checkbox)

    fun bind(garbage: GarbageGroup){
        onUpdate(garbage.type, checkboxWrapper)

        binding.name.text = garbage.name
        binding.root.alpha = garbage.alpha

        binding.root.setOnClickListener{
            onClick(garbage.type, checkboxWrapper)
        }
    }

    companion object{

        fun from(
            parent: ViewGroup,
            onUpdate: (GarbageType, Checkable) -> Unit,
            onClick: (GarbageType, Checkable) -> Unit
        ) : GarbageViewHolder {
            val binding = HeaderGarbageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return GarbageViewHolder(binding, onUpdate, onClick)
        }

    }

}