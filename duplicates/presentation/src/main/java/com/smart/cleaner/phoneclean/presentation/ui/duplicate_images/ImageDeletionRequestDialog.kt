package com.smart.cleaner.phoneclean.presentation.ui.duplicate_images

import androidx.fragment.app.viewModels
import com.smart.cleaner.phoneclean.presentation.ui.base.BaseDeletionRequestDialog

class ImageDeletionRequestDialog() : BaseDeletionRequestDialog() {

    override val viewModel: DuplicateImagesViewModel by viewModels()

    override fun onDelete() {
        viewModel.obtainEvent(ImagesStateScreen.ImageEvent.ConfirmedDeletion)
    }

}