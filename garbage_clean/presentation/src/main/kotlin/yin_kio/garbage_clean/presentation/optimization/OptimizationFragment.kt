package yin_kio.garbage_clean.presentation.optimization

import android.animation.Animator
import android.animation.ValueAnimator
import android.app.Dialog
import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.smart.cleaner.phoneclean.ui_core.adapters.OptimizingAdapter
import com.smart.cleaner.phoneclean.ui_core.adapters.models.GeneralOptimizingItem
import com.smart.cleaner.phoneclean.ui_core.databinding.FragmentBaseOptimizingBinding
import jamycake.lifecycle_aware.previousBackStackEntry
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import yin_kio.garbage_clean.presentation.R
import yin_kio.garbage_clean.presentation.garbage_list.Command
import yin_kio.garbage_clean.presentation.garbage_list.ViewModel
import kotlin.math.roundToInt

class OptimizationFragment : DialogFragment(com.smart.cleaner.phoneclean.ui_core.R.layout.fragment_base_optimizing) {

    private val binding: FragmentBaseOptimizingBinding by viewBinding()

    private val viewModel: ViewModel by previousBackStackEntry()

    private val adapter by lazy { OptimizingAdapter() }
    private var items = mutableListOf<GeneralOptimizingItem>()

    private val animator = getAnimator()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        setStyle(STYLE_NO_FRAME, general.R.style.Dialog)
        isCancelable = false
        return super.onCreateDialog(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.tvOptimizationTitle.text = getString(R.string.junk_clean_progress)

        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = object : LinearLayoutManager(requireContext()){
            override fun canScrollVertically(): Boolean {
                return false
            }
        }
        binding.recyclerView.itemAnimator = null

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.commands.collect {
                if (it == Command.ShowResult) {
                    findNavController().navigate(R.id.toResult)
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.state.first{
                items = it.cleanMessages.toMutableList()
                adapter.submitList(items as List<GeneralOptimizingItem>)
                runAnimation()
                true
            }
        }


    }

    private suspend fun runAnimation(){
        viewLifecycleOwner.lifecycleScope.launch {
            val totalDelay = 8000L
            val iteratinDelay = totalDelay / items.size
            repeat(items.size){
                delay(iteratinDelay)
                items.removeFirstOrNull()
                adapter.notifyItemRemoved(0)
            }
        }


        animator.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        animator.cancel()
    }

    private fun getAnimator() : Animator {
        return ValueAnimator.ofFloat(0f, 100f).apply {
            duration = 8500L

            addUpdateListener {
                val percent = ((it.animatedValue as Float)).roundToInt()
                binding.tvProgressPercents.text =
                    getString(general.R.string.value_percents, percent)
            }
        }
    }


}