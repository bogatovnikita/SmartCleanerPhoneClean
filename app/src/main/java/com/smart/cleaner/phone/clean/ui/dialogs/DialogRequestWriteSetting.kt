package com.smart.cleaner.phone.clean.ui.dialogs

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import by.kirich1409.viewbindingdelegate.viewBinding
import com.smart.cleaner.phone.clean.R
import com.smart.cleaner.phone.clean.databinding.DialogRequestWriteSettingBinding

class DialogRequestWriteSetting : DialogFragment (R.layout.dialog_request_write_setting) {

    private val binding: DialogRequestWriteSettingBinding by viewBinding()

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
        setFragmentResult(RESULT_WRITE_SETTING, bundleOf(RESULT_KEY to RESULT_SUCCESS))
    }

    private fun sendErrorResult() {
        setFragmentResult(RESULT_WRITE_SETTING, bundleOf(RESULT_KEY to RESULT_ERROR))
    }

    override fun onResume() {
        super.onResume()
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    companion object {
        const val RESULT_WRITE_SETTING = "RESULT_WRITE_SETTING"
        const val RESULT_KEY = "RESULT_KEY"
        const val RESULT_SUCCESS = "RESULT_SUCCESS"
        const val RESULT_ERROR = "RESULT_ERROR"
        const val TAG_WRITE_SETTING = "TAG_WRITE_SETTING"
    }

}
