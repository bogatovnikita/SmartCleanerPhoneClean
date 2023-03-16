package com.smart.cleaner.phoneclean.ui.base

import android.os.Bundle
import android.view.View
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.smart.cleaner.phoneclean.R
import com.smart.cleaner.phoneclean.adapters.ResultFunAdapter
import com.smart.cleaner.phoneclean.databinding.FragmentBaseResultBinding
import com.smart.cleaner.phoneclean.ui.result.FunResult
import com.smart.cleaner.phoneclean.utils.OptimizingType

abstract class BaseFragmentResult : Fragment(R.layout.fragment_base_result) {

    private val binding: FragmentBaseResultBinding by viewBinding()

    abstract fun setListFun(): List<FunResult>
    abstract fun setFunName(): String
    abstract fun setMessageOfCompleteFun(): String

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapter()
        renderState()
        showInterAndGoBack()
    }

    private fun renderState() {
        binding.tvFunName.text = setFunName()
        binding.tvDescriptionDangerOff.text = setMessageOfCompleteFun()
    }

    private fun showInterAndGoBack() {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            findNavController().popBackStack()
        }
    }

    private fun initAdapter() {
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
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter.submitList(setListFun())
    }

}