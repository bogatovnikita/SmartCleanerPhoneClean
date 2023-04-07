package com.smart.cleaner.phoneclean.presentation.adapters.listeners

import com.smart.cleaner.phoneclean.presentation.adapters.models.ChildFileItem
import com.smart.cleaner.phoneclean.presentation.adapters.models.ParentFileItem

interface OnFileChangeSelectListener {

    fun selectAll(duplicates: ParentFileItem, isSelected: Boolean)

    fun selectFile(file: ChildFileItem, isSelected: Boolean)

}