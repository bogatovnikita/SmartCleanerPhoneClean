package com.smart.cleaner.phoneclean.presentation.ui.duplicate_images

import androidx.fragment.app.activityViewModels
import com.smart.cleaner.phoneclean.presentation.ui.base.BaseDeletionRequestDialog
import java.util.Calendar

class ImageDeletionRequestDialog: BaseDeletionRequestDialog() {

    override val viewModel: DuplicateImagesViewModel by activityViewModels()

    override fun onDelete() {
        viewModel.obtainEvent(ImagesStateScreen.ImageEvent.Delete(Calendar.getInstance().timeInMillis))
    }

}