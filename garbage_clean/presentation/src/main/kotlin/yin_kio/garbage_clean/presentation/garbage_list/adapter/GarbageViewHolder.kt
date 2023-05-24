package yin_kio.garbage_clean.presentation.garbage_list.adapter

import android.animation.ValueAnimator
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import androidx.core.view.isVisible
import pokercc.android.expandablerecyclerview.ExpandableAdapter
import yin_kio.garbage_clean.domain.services.garbage_files.GarbageType
import yin_kio.garbage_clean.domain.ui_out.Checkable
import yin_kio.garbage_clean.presentation.databinding.HeaderGarbageBinding
import yin_kio.garbage_clean.presentation.garbage_list.adapter.models.GarbageGroup

class GarbageViewHolder private constructor(
    val binding: HeaderGarbageBinding,
    private val onUpdate: (GarbageType, Checkable) -> Unit,
    private val onCheckboxClick: (GarbageType, Checkable) -> Unit
) : ExpandableAdapter.ViewHolder(binding.root) {

    private val checkboxWrapper = CheckboxWrapper(binding.checkbox)

    fun bind(garbage: GarbageGroup,
             isExpand: Boolean,
             onExpandClick: () -> Unit
    ){
        onUpdate(garbage.type, checkboxWrapper)

        binding.name.text = garbage.name
        binding.description.text = garbage.description
        binding.root.alpha = garbage.alpha

        binding.progress.isVisible = garbage.isInProgress
        binding.expand.rotation = if (isExpand) 180f else 0f



        startProgressAnimationIfNeed(garbage)

        binding.checkbox.isVisible = !garbage.isInProgress
        binding.expand.isVisible = !garbage.isInProgress

        binding.checkbox.isEnabled = garbage.isEnabled

        binding.checkbox.setOnClickListener{
            onCheckboxClick(garbage.type, checkboxWrapper)
        }
        binding.expand.setOnClickListener {
            if (garbage.isEnabled) onExpandClick()
        }
    }

    private fun startProgressAnimationIfNeed(garbage: GarbageGroup) {
        if (garbage.isInProgress) {
            val animator = ValueAnimator.ofFloat(0f, 360f).apply {
                duration = 1000
                interpolator = LinearInterpolator()
                addUpdateListener {
                    binding.progress.rotation = it.animatedValue as Float
                }
                repeatCount = 10000
            }

            animator.start()
        }
    }

    companion object{

        fun from(
            parent: ViewGroup,
            onUpdate: (GarbageType, Checkable) -> Unit,
            onClick: (GarbageType, Checkable) -> Unit
        ) : GarbageViewHolder {
            val binding = HeaderGarbageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return GarbageViewHolder(
                binding = binding,
                onUpdate = onUpdate,
                onCheckboxClick = onClick
            )
        }

    }

}