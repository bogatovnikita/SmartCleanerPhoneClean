package com.smart.cleaner.phoneclean.domain.use_case.images

import com.smart.cleaner.phoneclean.domain.extentions.findDuplicates
import com.smart.cleaner.phoneclean.domain.gateways.DuplicatesSettings
import com.smart.cleaner.phoneclean.domain.gateways.Files
import com.smart.cleaner.phoneclean.domain.gateways.ImagesComparator
import com.smart.cleaner.phoneclean.domain.gateways.Permissions
import com.smart.cleaner.phoneclean.domain.models.ImageInfo
import com.smart.cleaner.phoneclean.domain.use_case.BaseDuplicateUseCase
import javax.inject.Inject

class DuplicateImagesUseCaseImpl @Inject constructor(
    private val files: Files,
    private val permissions: Permissions,
    private val settings: DuplicatesSettings,
    private val imagesComparator: ImagesComparator,
) : BaseDuplicateUseCase(files, permissions, settings), DuplicateImagesUseCase {

    override suspend fun getImageDuplicates(): List<List<ImageInfo>> =
        files.getImages().findDuplicates(imagesComparator)

}