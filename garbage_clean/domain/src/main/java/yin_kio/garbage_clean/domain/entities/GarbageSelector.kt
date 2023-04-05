package yin_kio.garbage_clean.domain.entities

import yin_kio.garbage_clean.domain.services.garbage_files.GarbageType
import yin_kio.garbage_clean.domain.services.selectable_form.SelectableForm
import java.io.File

internal interface GarbageSelector {

    fun setGarbage(garbage: Map<GarbageType, SelectableForm<File>>)

    fun switchFileSelection(group: GarbageType, file: File)
    fun isItemSelected(group: GarbageType, item: File) : Boolean
    fun switchGroupSelected(group: GarbageType)
    fun isGroupSelected(group: GarbageType) : Boolean

    fun getGarbage() : Map<GarbageType, SelectableForm<File>>

    fun getSelected() : List<File>

    val hasSelected: Boolean



}