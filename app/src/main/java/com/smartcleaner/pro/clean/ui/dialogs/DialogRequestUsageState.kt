package com.smartcleaner.pro.clean.ui.dialogs

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import by.kirich1409.viewbindingdelegate.viewBinding
import com.smartcleaner.pro.clean.R
import com.smartcleaner.pro.clean.databinding.DialogRequestUsageStateBinding

class DialogRequestUsageState : DialogFragment(R.layout.dialog_request_usage_state) {
    private val binding: DialogRequestUsageStateBinding by viewBinding()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, R.style.DialogStyle)
    }

    override fun onResume() {
        super.onResume()
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
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
        setFragmentResult(
            RESULT_USAGE_STATE,
            bundleOf(RESULT_USAGE_STATE_KEY to RESULT_USAGE_STATE_SUCCESS)
        )
    }

    private fun sendErrorResult() {
        setFragmentResult(
            RESULT_USAGE_STATE_KEY,
            bundleOf(RESULT_USAGE_STATE_KEY to RESULT_USAGE_STATE_ERROR)
        )
    }

    companion object {
        const val RESULT_USAGE_STATE = "RESULT_USAGE_STATE"
        const val RESULT_USAGE_STATE_KEY = "RESULT_USAGE_STATE_KEY"
        const val RESULT_USAGE_STATE_SUCCESS = "RESULT_USAGE_STATE_SUCCESS"
        const val RESULT_USAGE_STATE_ERROR = "RESULT_USAGE_STATE_ERROR"
        const val TAG_USAGE_STATE = "TAG_USAGE_STATE"
    }
}