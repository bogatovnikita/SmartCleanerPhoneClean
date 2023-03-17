package com.smart.cleaner.phoneclean.ui.base

import android.app.Dialog
import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.smart.cleaner.phoneclean.R
import com.smart.cleaner.phoneclean.adapters.ResultFunAdapter
import com.smart.cleaner.phoneclean.ui.result.FunResult
import com.smart.cleaner.phoneclean.utils.OptimizingType

abstract class BaseFragmentResult(layout: Int) : DialogFragment(layout) {

    abstract fun setListFun(): List<FunResult>
    abstract fun setRecyclerView(): RecyclerView

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        setStyle(STYLE_NO_FRAME, R.style.Dialog)
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
                    OptimizingType.Boost -> findNavController().navigate(R.id.action_to_boostFragment)
                    OptimizingType.Clean -> {}
                    OptimizingType.Cooling -> {}
                    OptimizingType.Battery -> findNavController().navigate(R.id.action_to_batteryFragment)
                }
            }

        })
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter.submitList(setListFun())
    }

}