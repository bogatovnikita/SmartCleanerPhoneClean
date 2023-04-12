package com.smartcleaner.pro.clean.ui.dialogs

import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.smartcleaner.pro.clean.R
import com.smartcleaner.pro.clean.databinding.DialogStartFirstLanguageBinding

class ShowStartLanguageDialog : DialogFragment(R.layout.dialog_start_first_language) {

    private val binding: DialogStartFirstLanguageBinding by viewBinding()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, R.style.DialogStyle)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnCancelDialog.setOnClickListener {
            dismiss()
        }
    }

}