package yin_kio.garbage_clean.domain.out

data class DeleteFormOut(
    val isAllSelected: Boolean = false,
    val canDelete: Boolean = false,
    val items: List<DeleteFormOutItem> = listOf()
)
