package com.smart.cleaner.phoneclean.presentation.ui.dialogs

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.smart.cleaner.phoneclean.presentation.R
import com.smart.cleaner.phoneclean.presentation.databinding.FragmentDeletionRequestDialogBinding
import com.smart.cleaner.phoneclean.presentation.ui.duplicate_images.DuplicateImagesViewModel
import com.smart.cleaner.phoneclean.presentation.ui.duplicates_files.DuplicateFilesViewModel
import com.smart.cleaner.phoneclean.presentation.ui.models.FilesStateScreen
import com.smart.cleaner.phoneclean.presentation.ui.models.ImagesStateScreen
import dagger.hilt.android.AndroidEntryPoint
import java.util.Calendar

@AndroidEntryPoint
class DeletionRequestDialog : DialogFragment(R.layout.fragment_deletion_request_dialog) {

    private val imageViewModel: DuplicateImagesViewModel by activityViewModels()

    private val fileViewModel: DuplicateFilesViewModel by activityViewModels()

    private val binding: FragmentDeletionRequestDialogBinding by viewBinding()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, R.style.DialogStyle)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnDelete.setOnClickListener {
            onDelete()
            findNavController().navigate(R.id.action_to_optimizingFragment)
        }
        binding.btnClose.setOnClickListener {
            dismiss()
        }
    }

    override fun onResume() {
        super.onResume()
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    private fun onDelete() {
        val time = Calendar.getInstance().timeInMillis
        imageViewModel.obtainEvent(ImagesStateScreen.ImageEvent.Delete(time))
        fileViewModel.obtainEvent(FilesStateScreen.FileEvent.Delete(time))
    }

}