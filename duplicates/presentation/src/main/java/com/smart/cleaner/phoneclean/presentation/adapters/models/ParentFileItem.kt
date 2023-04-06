package com.smart.cleaner.phoneclean.presentation.adapters.models

data class ParentFileItem(
    val count: Int = 0,
    val isAllSelected: Boolean = false,
    val files: List<ChildFileItem> = emptyList()
)