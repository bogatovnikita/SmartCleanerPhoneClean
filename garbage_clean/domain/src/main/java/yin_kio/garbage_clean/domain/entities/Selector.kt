package yin_kio.garbage_clean.domain.entities

interface Selector {

    fun switchItemSelection(groupIndex: Int, itemIndex: Int)
    fun isItemSelected(groupIndex: Int, itemIndex: Int) : Boolean
    fun switchGroupSelected(groupIndex: Int)
    fun isGroupSelected(groupIndex: Int) : Boolean


}