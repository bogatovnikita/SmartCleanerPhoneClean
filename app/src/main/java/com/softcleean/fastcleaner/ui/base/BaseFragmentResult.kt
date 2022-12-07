package com.softcleean.fastcleaner.ui.base

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.softcleean.fastcleaner.R
import com.softcleean.fastcleaner.adapters.ResultFunAdapter
import com.softcleean.fastcleaner.databinding.FragmentBaseResultBinding
import com.softcleean.fastcleaner.ui.result.FunResult
import com.softcleean.fastcleaner.utils.OptimizingType

abstract class BaseFragmentResult: Fragment(R.layout.fragment_base_result) {

    private val binding: FragmentBaseResultBinding by viewBinding()

    abstract fun setListFun(): List<FunResult>
    abstract fun setFunName(): String
    abstract fun setMessageOfCompleteFun(): String

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapter()
        renderState()
    }

    private fun renderState() {
        binding.tvFunName.text = setFunName()
        binding.tvDescriptionDangerOff.text = setMessageOfCompleteFun()
    }

    private fun initAdapter() {
        val adapter = ResultFunAdapter(object : ResultFunAdapter.ClickOnFunResultListener {
            override fun onFunClick(item: FunResult) {
                when (item.type) {
                    OptimizingType.Boost -> findNavController().navigate(R.id.action_to_boostFragment)
                    OptimizingType.Clean -> findNavController().navigate(R.id.action_to_cleanFragment)
                    OptimizingType.Cooling -> findNavController().navigate(R.id.action_to_coolingFragment)
                    OptimizingType.Battery -> findNavController().navigate(R.id.action_to_batteryFragment)
                }
            }

        })
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter.submitList(setListFun())
    }

}