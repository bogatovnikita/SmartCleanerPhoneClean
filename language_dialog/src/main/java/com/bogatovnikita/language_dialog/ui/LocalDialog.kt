package com.bogatovnikita.language_dialog.ui

import android.app.Application
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.GridLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bogatovnikita.language_dialog.R
import com.bogatovnikita.language_dialog.adapter.LocalAdapter
import com.bogatovnikita.language_dialog.databinding.DialogLocalBinding
import com.bogatovnikita.language_dialog.utils.LocaleProvider
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LocalDialog : DialogFragment(R.layout.dialog_local) {

    @Inject
    lateinit var localeProvider: LocaleProvider

    @Inject
    lateinit var context: Application

    private val binding: DialogLocalBinding by viewBinding()

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
        initLocales()
    }

    private fun initLocales() {
        val adapter = LocalAdapter {
            localeProvider.saveNewLocale(it)
            dismiss()
            requireActivity().finish()
            startActivity(requireActivity().intent)
        }
        binding.recyclerView.layoutManager = GridLayoutManager(context, 3)
        binding.recyclerView.adapter = adapter
    }
}