package com.smart.cleaner.phoneclean.presentation.adapters.listeners

import com.smart.cleaner.phoneclean.presentation.adapters.models.ChildImageItem
import com.smart.cleaner.phoneclean.presentation.adapters.models.ParentImageItem

interface OnChangeSelectListener {

    fun selectAll(duplicates: ParentImageItem, isSelected: Boolean)

    fun selectImage(image: ChildImageItem, isSelected: Boolean)

}