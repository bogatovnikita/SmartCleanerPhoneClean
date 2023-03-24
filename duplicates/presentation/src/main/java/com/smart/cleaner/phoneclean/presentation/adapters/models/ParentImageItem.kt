package com.smart.cleaner.phoneclean.presentation.adapters.models

data class ParentImageItem(
    val count: Int = 0,
    val isAllSelected: Boolean = false,
    val images: List<ChildImageItem> = emptyList()
)