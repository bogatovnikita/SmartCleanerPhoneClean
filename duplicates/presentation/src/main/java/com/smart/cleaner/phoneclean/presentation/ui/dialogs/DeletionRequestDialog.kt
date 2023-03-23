package com.smart.cleaner.phoneclean.presentation.ui.dialogs

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.smart.cleaner.phoneclean.presentation.R
import com.smart.cleaner.phoneclean.presentation.databinding.FragmentDeletionRequestDialogBinding

class DeletionRequestDialog : DialogFragment(R.layout.fragment_deletion_request_dialog) {

    private val binding: FragmentDeletionRequestDialogBinding by viewBinding()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.DialogStyle)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnDelete.setOnClickListener {
            sendSuccessResult()
            dismiss()
        }
        binding.btnClose.setOnClickListener {
            sendErrorResult()
            dismiss()
        }
    }

    private fun sendSuccessResult() {
    }

    private fun sendErrorResult() {
    }

    override fun onResume() {
        super.onResume()
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

}