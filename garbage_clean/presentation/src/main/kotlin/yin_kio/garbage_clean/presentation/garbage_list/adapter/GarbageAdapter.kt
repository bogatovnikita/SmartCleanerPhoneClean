package yin_kio.garbage_clean.presentation.garbage_list.adapter

import android.view.ViewGroup
import pokercc.android.expandablerecyclerview.ExpandableAdapter
import yin_kio.garbage_clean.domain.services.garbage_files.GarbageType
import yin_kio.garbage_clean.domain.ui_out.Checkable
import yin_kio.garbage_clean.presentation.garbage_list.adapter.models.GarbageGroup
import java.io.File

class GarbageAdapter(
    private val onItemUpdate: (GarbageType, file: File, Checkable) -> Unit,
    private val onGroupUpdate: (GarbageType, Checkable) -> Unit,
    private val onItemClick: (GarbageType, file: File, Checkable) -> Unit,
    private val onGroupClick: (GarbageType, Checkable) -> Unit
) : ExpandableAdapter<ExpandableAdapter.ViewHolder>() {

    var garbage: List<GarbageGroup> = listOf()

    var onExpandListenerEnabled: Boolean = true

    override fun getChildCount(groupPosition: Int): Int {
        if (garbage.isEmpty()) return 0
        return garbage[groupPosition].files.size
    }

    override fun getGroupCount(): Int {
        return garbage.size
    }

    override fun onBindChildViewHolder(
        holder: ViewHolder,
        groupPosition: Int,
        childPosition: Int,
        payloads: List<Any>
    ) {
        val garbageGroup = garbage[groupPosition]
        (holder as? FileViewHolder)?.bind(garbageGroup.type, garbageGroup.files[childPosition])
    }

    override fun onBindGroupViewHolder(
        holder: ViewHolder,
        groupPosition: Int,
        expand: Boolean,
        payloads: List<Any>
    ) {
        (holder as? GarbageViewHolder)?.bind(garbage[groupPosition],
            isExpand = expand,
            onExpandClick = {
                if (isExpand(groupPosition)){
                    collapseGroup(groupPosition, true)
                } else {
                    expandGroup(groupPosition, true)
                }
            }
        )
    }

    override fun onCreateChildViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        return FileViewHolder.from(
            parent = viewGroup,
            onClick = onItemClick,
            onUpdate = onItemUpdate
        )
    }

    override fun onCreateGroupViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        return GarbageViewHolder.from(
            parent = viewGroup,
            onUpdate = onGroupUpdate,
            onClick = onGroupClick
        )
    }

    override fun onGroupViewHolderExpandChange(
        holder: ViewHolder,
        groupPosition: Int,
        animDuration: Long,
        expand: Boolean
    ) {
        if (onExpandListenerEnabled){
            val rotation = if (expand) 180f else 0f
            (holder as? GarbageViewHolder)?.binding?.expand?.rotation = rotation
        }
    }
}