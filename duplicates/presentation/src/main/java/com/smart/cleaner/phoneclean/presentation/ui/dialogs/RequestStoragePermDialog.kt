package com.smart.cleaner.phoneclean.presentation.ui.dialogs

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.smart.cleaner.phoneclean.presentation.R
import com.smart.cleaner.phoneclean.presentation.databinding.FragmentRequestStoragePermDialogBinding

class RequestStoragePermDialog : DialogFragment(R.layout.fragment_request_storage_perm_dialog) {

    private val binding: FragmentRequestStoragePermDialogBinding by viewBinding()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, R.style.DialogStyle)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnOk.setOnClickListener {
            sendSuccessResult()
            dismiss()
        }
        binding.btnCancelDialog.setOnClickListener {
            sendErrorResult()
            dismiss()
        }
        binding.btnCancel.setOnClickListener {
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