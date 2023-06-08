package com.smart.cleaner.phoneclean.ui_core.adapters.base

import android.app.Dialog
import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.smart.cleaner.phoneclean.ui_core.adapters.GetIdForNavigation
import com.smart.cleaner.phoneclean.ui_core.adapters.ResultFunAdapter
import com.smart.cleaner.phoneclean.ui_core.adapters.ResultList
import com.smart.cleaner.phoneclean.ui_core.adapters.models.FunResult
import com.smart.cleaner.phoneclean.ui_core.adapters.models.OptimizingType

abstract class BaseFragmentResult(layout: Int) : DialogFragment(layout) {

    abstract val typeResult: OptimizingType
    abstract fun setRecyclerView(): RecyclerView

    private val resultList: ResultList by lazy { ResultList(requireContext()) }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        setStyle(STYLE_NO_FRAME, general.R.style.Dialog)
        return super.onCreateDialog(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapter()
    }

    private fun initAdapter() {
        val recyclerView = setRecyclerView()
        val adapter = ResultFunAdapter(object : ResultFunAdapter.ClickOnFunResultListener {
            override fun onFunClick(item: FunResult) {
                when (item.type) {
                    OptimizingType.Boost -> (requireActivity() as GetIdForNavigation).openBoostMenu()
                    OptimizingType.Clean -> (requireActivity() as GetIdForNavigation).openCleanMenu()
                    OptimizingType.Duplicates -> (requireActivity() as GetIdForNavigation).openDuplicatesMenu()
                    OptimizingType.Battery -> (requireActivity() as GetIdForNavigation).openBatteryMenu()
                }
            }

        })
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter.submitList(resultList.getList(typeResult))
    }
}