package yin_kio.garbage_clean.domain.services.selectable_form

internal interface SelectableForm<T> {

    var content: Collection<T>
    val selected: Collection<T>
    val isAllSelected: Boolean

    fun switchItemSelection(item: T)
    fun switchAllSelection()

    fun isItemSelected(item: T) : Boolean

}