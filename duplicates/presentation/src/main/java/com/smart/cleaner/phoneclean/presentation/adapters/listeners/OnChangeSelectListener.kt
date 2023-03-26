package com.smart.cleaner.phoneclean.presentation.adapters.listeners

import com.smart.cleaner.phoneclean.presentation.adapters.models.ChildImageItem

interface OnChangeSelectListener {

    fun selectAll(duplicates: List<ChildImageItem>, isSelected: Boolean)

    fun selectImage(image: ChildImageItem, isSelected: Boolean)

}