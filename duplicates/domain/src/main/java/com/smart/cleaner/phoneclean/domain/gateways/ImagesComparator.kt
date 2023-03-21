package com.smart.cleaner.phoneclean.domain.gateways

import com.smart.cleaner.phoneclean.domain.models.ImageInfo

interface ImagesComparator {

    fun compareImages(imageFirst: ImageInfo, imageSecond:  ImageInfo):Boolean

}