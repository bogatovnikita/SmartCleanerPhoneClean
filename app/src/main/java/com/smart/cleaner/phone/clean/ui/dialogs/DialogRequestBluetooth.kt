package com.smart.cleaner.phone.clean.ui.dialogs

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import by.kirich1409.viewbindingdelegate.viewBinding
import com.smart.cleaner.phone.clean.R
import com.smart.cleaner.phone.clean.databinding.DialogRequestBluetoothBinding

class DialogRequestBluetooth : DialogFragment(R.layout.dialog_request_bluetooth) {

    private val binding: DialogRequestBluetoothBinding by viewBinding()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, R.style.DialogStyle)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.setCanceledOnTouchOutside(false)
        return dialog
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnOk.setOnClickListener {
            sendSuccessResult()
            dismiss()
        }
        binding.btnCancelDialog.setOnClickListener {
            sendSuccessResult()
//            sendErrorResult()
            dismiss()
        }
    }

    private fun sendSuccessResult() {
        setFragmentResult(
            RESULT_BLUETOOTH,
            bundleOf(RESULT_KEY_BLUETOOTH to RESULT_SUCCESS_BLUETOOTH)
        )
    }

    private fun sendErrorResult() {
        setFragmentResult(
            RESULT_BLUETOOTH,
            bundleOf(RESULT_KEY_BLUETOOTH to RESULT_ERROR_BLUETOOTH)
        )
    }

    override fun onResume() {
        super.onResume()
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    companion object {
        const val RESULT_BLUETOOTH = "RESULT_WRITE_SETTING"
        const val RESULT_KEY_BLUETOOTH = "RESULT_KEY_BLUETOOTH"
        const val RESULT_SUCCESS_BLUETOOTH = "RESULT_SUCCESS_BLUETOOTH"
        const val RESULT_ERROR_BLUETOOTH = "RESULT_ERROR_BLUETOOTH"
        const val TAG_BLUETOOTH = "TAG_BLUETOOTH"
    }

}
