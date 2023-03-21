package com.smart.cleaner.phoneclean.ui.battery

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bogatovnikita.language_dialog.ui.LocalDialog
import com.bogatovnikita.language_dialog.utils.LocaleProvider
import com.smart.cleaner.phoneclean.R
import com.smart.cleaner.phoneclean.databinding.FragmentBatteryResultBinding
import com.smart.cleaner.phoneclean.ui.base.BaseFragmentResult
import com.smart.cleaner.phoneclean.ui.result.FunResult
import com.smart.cleaner.phoneclean.ui.result.ResultList
import com.smart.cleaner.phoneclean.utils.OptimizingType
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class BatteryResultFragment : BaseFragmentResult(R.layout.fragment_battery_result) {

    private val binding: FragmentBatteryResultBinding by viewBinding()

    @Inject
    lateinit var resultList: ResultList

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initLocale()
        initListeners()
    }

    private fun initLocale() {
        binding.btnChangeLanguage.setImageResource(LocaleProvider(requireContext()).getCurrentLocaleModel().image)
    }

    private fun initListeners() {
        binding.btnGoBack.setOnClickListener { findNavController().popBackStack() }
        binding.btnChangeLanguage.setOnClickListener { openLocalDialog() }
    }

    private fun openLocalDialog() {
        val dialog = LocalDialog(requireContext()) {
            val intent: Intent = requireActivity().intent
            requireActivity().finish()
            startActivity(intent)
        }
        dialog.show()
    }

    override fun setListFun(): List<FunResult> =
        resultList.getList().filter { it.type != OptimizingType.Battery }

    override fun setRecyclerView(): RecyclerView = binding.recyclerView

}