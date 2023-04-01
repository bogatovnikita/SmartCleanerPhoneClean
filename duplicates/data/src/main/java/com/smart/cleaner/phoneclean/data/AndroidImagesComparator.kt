package com.smart.cleaner.phoneclean.data

import android.graphics.BitmapFactory
import com.smart.cleaner.phoneclean.domain.gateways.ImagesComparator
import com.smart.cleaner.phoneclean.domain.models.ImageInfo
import java.io.File
import javax.inject.Inject

class AndroidImagesComparator @Inject constructor(): ImagesComparator {

    override fun invoke(imageFirst: ImageInfo, imageSecond: ImageInfo): Boolean {
        var isContentEquals = false
        try {
            val first = File(imageFirst.path)
            val second = File(imageSecond.path)

            if (first.name == second.name){
                isContentEquals = isContentEquals(first, second)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return isContentEquals
    }

    private fun isContentEquals(first: File, second: File): Boolean {
        val firstBitmap = BitmapFactory.decodeFile(first.absolutePath)
        val secondBitmap = BitmapFactory.decodeFile(second.absolutePath)
        return firstBitmap.sameAs(secondBitmap)
    }

}