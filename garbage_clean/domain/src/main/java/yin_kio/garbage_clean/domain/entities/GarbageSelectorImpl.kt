package yin_kio.garbage_clean.domain.entities

import yin_kio.garbage_clean.domain.services.garbage_files.GarbageType
import yin_kio.garbage_clean.domain.services.selectable_form.SelectableForm
import yin_kio.garbage_clean.domain.ui_out.UiOut
import java.io.File

internal class GarbageSelectorImpl : GarbageSelector {

    private var garbage: Map<GarbageType, SelectableForm<File>> = mapOf()

    override fun setGarbage(garbage: Map<GarbageType, SelectableForm<File>>) {
        this.garbage = garbage
    }

    override fun switchFileSelection(group: GarbageType, file: File) {
        assert(garbage[group] != null)
        garbage[group]!!.switchItemSelection(file)
    }

    override fun isItemSelected(group: GarbageType, item: File): Boolean {
        if (garbage[group] == null) return false
        return garbage[group]!!.isItemSelected(item)
    }

    override fun switchGroupSelected(group: GarbageType) {
        if (garbage[group] == null) return
        garbage[group]!!.switchAllSelection()
    }

    override fun isGroupSelected(group: GarbageType): Boolean {
        if (garbage[group] == null) return false
        return garbage[group]!!.isAllSelected
    }

    override fun getGarbage(): Map<GarbageType, SelectableForm<File>> {
        return garbage
    }

    override fun getSelected(): List<File> {
        val selected = mutableListOf<File>()

        garbage.forEach {
            selected.addAll(it.value.selected)
        }

        return selected
    }

    override val hasSelected: Boolean
        get() {
            garbage.forEach {
                if (it.value.selected.isNotEmpty()) return  true
            }
            return false
        }

    override var uiOut: UiOut = UiOut.Init
}