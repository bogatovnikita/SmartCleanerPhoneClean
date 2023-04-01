package yin_kio.garbage_clean.domain.entities

import yin_kio.garbage_clean.domain.services.garbage_files.GarbageType
import yin_kio.garbage_clean.domain.services.selectable_form.SelectableForm
import java.io.File

class GarbageSelectorImpl : GarbageSelector {

    private var garbage: Map<GarbageType, SelectableForm<File>> = mapOf()

    override fun setGarbage(garbage: Map<GarbageType, SelectableForm<File>>) {
        this.garbage = garbage
    }

    override fun switchFileSelection(group: GarbageType, file: File) {
        assert(garbage[group] != null)
        garbage[group]!!.switchItemSelection(file)
    }

    override fun isItemSelected(group: GarbageType, item: File): Boolean {
        assert(garbage[group] != null)
        return garbage[group]!!.isItemSelected(item)
    }

    override fun switchGroupSelected(group: GarbageType) {
        assert(garbage[group] != null)
        garbage[group]!!.switchAllSelection()
    }

    override fun isGroupSelected(group: GarbageType): Boolean {
        assert(garbage[group] != null)
        return garbage[group]!!.isAllSelected
    }

    override fun getGarbage(): Map<GarbageType, SelectableForm<File>> {
        return garbage
    }
}