package com.smart.cleaner.phoneclean.data

import com.smart.cleaner.phoneclean.domain.gateways.FilesComparator
import com.smart.cleaner.phoneclean.domain.models.File
import javax.inject.Inject

class FileComparatorImpl @Inject constructor(): FilesComparator {

    override fun invoke(file1: File, file2: File): Boolean {
        var isContentEquals = false
        try {
            val first = java.io.File(file1.path)
            val second = java.io.File(file2.path)

            isContentEquals = (first.name == second.name)

        } catch (e: Exception) {
            e.printStackTrace()
        }
        return isContentEquals
    }

}