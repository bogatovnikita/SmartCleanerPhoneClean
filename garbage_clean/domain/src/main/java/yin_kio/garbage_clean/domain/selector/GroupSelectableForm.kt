package yin_kio.garbage_clean.domain.selector

open class GroupSelectableForm<T> : SelectableForm<SelectableForm<T>> by BaseSelectableForm() {

    override val isAllSelected: Boolean
        get() {

            content.forEach {
                if (!it.isAllSelected){
                    return false
                }
            }

            return true
        }
}