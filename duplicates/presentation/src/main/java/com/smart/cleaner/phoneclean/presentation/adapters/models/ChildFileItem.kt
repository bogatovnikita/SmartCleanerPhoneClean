package com.smart.cleaner.phoneclean.presentation.adapters.models

data class ChildFileItem(
    val isSelected: Boolean = false,
    val filePath: String = "",
    val fileName: String = "",
    val size: Long = 0,
)