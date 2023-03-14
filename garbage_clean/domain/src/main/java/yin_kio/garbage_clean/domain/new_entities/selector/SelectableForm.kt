package yin_kio.garbage_clean.domain.new_entities.selector

interface SelectableForm<T> {

    var content: Collection<T>
    val selected: Collection<T>
    val isAllSelected: Boolean

    fun switchItemSelection(item: T)
    fun switchAllSelection()

    fun isItemSelected(item: T) : Boolean

}