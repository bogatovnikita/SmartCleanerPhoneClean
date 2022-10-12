package com.entertainment.event.ssearch.ar155.functions.result

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.entertainment.event.ssearch.ar155.R
import com.entertainment.event.ssearch.ar155.adapters.ResultFunAdapter
import com.entertainment.event.ssearch.ar155.utils.OptimizingType

abstract class BaseResultFragment<VB : ViewBinding>(
    private val bindingInflater: (inflater: LayoutInflater, container: ViewGroup?, attachToRoot: Boolean) -> VB
) : Fragment() {

    private var _binding: VB? = null

    val binding: VB
        get() = _binding as VB

    protected lateinit var adapter: ResultFunAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = bindingInflater.invoke(inflater, container, false)
        if (_binding == null) {
            throw IllegalArgumentException("Binding cannot be null")
        }
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    protected fun setBtnListeners(btnGoMain: AppCompatButton, btnGoBack: ImageButton) {
        btnGoMain.setOnClickListener {
            findNavController().navigate(R.id.action_to_homeFragment)
        }
        btnGoBack.setOnClickListener {
            findNavController().navigate(R.id.action_to_homeFragment)
        }
    }

    protected fun initAdapter(recyclerView: RecyclerView) {
        adapter = ResultFunAdapter(object : ResultFunAdapter.ClickOnFunResultListener {
            override fun onFunClick(item: FunResult) {
                when (item.type) {
                    OptimizingType.Boost -> findNavController().navigate(R.id.action_to_boostFragment)
                    OptimizingType.Clean -> findNavController().navigate(R.id.action_to_cleanFragment)
                    OptimizingType.Cooling -> findNavController().navigate(R.id.action_to_coolingFragment)
                    OptimizingType.Battery -> findNavController().navigate(R.id.action_to_batteryFragment)
                }
            }

        })
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

}