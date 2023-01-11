package com.softcleean.fastcleaner.ui.base

import android.os.Bundle
import android.view.View
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.softcleean.fastcleaner.R
import com.softcleean.fastcleaner.adapters.ResultFunAdapter
import com.softcleean.fastcleaner.ads.AdsViewModel
import com.softcleean.fastcleaner.databinding.FragmentBaseResultBinding
import com.softcleean.fastcleaner.ui.result.FunResult
import com.softcleean.fastcleaner.utils.OptimizingType
import showInterstitial

abstract class BaseFragmentResult : Fragment(R.layout.fragment_base_result) {

    private val binding: FragmentBaseResultBinding by viewBinding()
    private val viewModel: AdsViewModel by activityViewModels()

    abstract fun setListFun(): List<FunResult>
    abstract fun setFunName(): String
    abstract fun setMessageOfCompleteFun(): String

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapter()
        renderState()
        showInterAndGoBack()
    }

    override fun onResume() {
        super.onResume()
        viewModel.canShowInter()
    }

    override fun onPause() {
        super.onPause()
        viewModel.cantShowInter()
    }

    private fun renderState() {
        binding.tvFunName.text = setFunName()
        binding.tvDescriptionDangerOff.text = setMessageOfCompleteFun()
    }

    private fun showInterAndGoBack() {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            findNavController().popBackStack()
            showInterstitial()
        }
    }

    private fun initAdapter() {
        val adapter = ResultFunAdapter(object : ResultFunAdapter.ClickOnFunResultListener {
            override fun onFunClick(item: FunResult) {
                when (item.type) {
                    OptimizingType.Boost -> showAdsAndNavigate(R.id.action_to_boostFragment)
                    OptimizingType.Clean -> showAdsAndNavigate(R.id.action_to_cleanFragment)
                    OptimizingType.Cooling -> showAdsAndNavigate(R.id.action_to_coolingFragment)
                    OptimizingType.Battery -> showAdsAndNavigate(R.id.action_to_batteryFragment)
                }
            }

        })
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter.submitList(setListFun())
    }

    private fun showAdsAndNavigate(navigateId: Int) {
        showInterstitial(
            onClosed = { findNavController().navigate(navigateId) }
        )
    }

}