package yin_kio.garbage_clean.domain.entities

import yin_kio.garbage_clean.domain.ui_out.Garbage

interface GarbageSelector {

    fun switchItemSelection(groupIndex: Int, itemIndex: Int)
    fun isItemSelected(groupIndex: Int, itemIndex: Int) : Boolean
    fun switchGroupSelected(groupIndex: Int)
    fun isGroupSelected(groupIndex: Int) : Boolean

    fun getGarbage() : List<Garbage>



}