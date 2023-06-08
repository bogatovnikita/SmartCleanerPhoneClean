package com.smart.cleaner.phone.clean.ui.dialogs

import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.smart.cleaner.phone.clean.R
import com.smart.cleaner.phone.clean.databinding.FragmentDialogTimeOutBinding

class DialogTimeOut : DialogFragment(R.layout.fragment_dialog_time_out) {

    val binding: FragmentDialogTimeOutBinding by viewBinding()

    override fun onCreate(savedInstanceState: Bundle?) {
        setStyle(STYLE_NO_TITLE, R.style.DialogTimeOut)
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnCancelDialog.setOnClickListener {
            findNavController().popBackStack()
        }
    }

}