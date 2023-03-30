package com.smart.cleaner.phoneclean.ui_core.adapters.base

import Const.DEEP_LINK_TO_BATTERY
import Const.DEEP_LINK_TO_BOOST
import Const.DEEP_LINK_TO_DUPLICATES
import android.app.Dialog
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
                    OptimizingType.Boost -> navigateWithDeppLink(DEEP_LINK_TO_BOOST)
                    OptimizingType.Clean -> {}
                    OptimizingType.Duplicates -> navigateWithDeppLink(DEEP_LINK_TO_DUPLICATES)
                    OptimizingType.Battery -> navigateWithDeppLink(DEEP_LINK_TO_BATTERY)
                }
            }

        })
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter.submitList(resultList.getList(typeResult))
    }

    private fun navigateWithDeppLink(deepLink: String) {
        val uri = Uri.parse(deepLink)
        findNavController().navigate(uri)
    }

}