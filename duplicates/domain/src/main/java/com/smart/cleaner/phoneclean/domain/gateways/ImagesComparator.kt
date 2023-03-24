package com.smart.cleaner.phoneclean.domain.gateways

import com.smart.cleaner.phoneclean.domain.models.ImageInfo


interface ImagesComparator:  (ImageInfo, ImageInfo) -> Boolean