package yin_kio.garbage_clean.presentation.result

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.smart.cleaner.phoneclean.settings.Settings
import com.smart.cleaner.phoneclean.ui_core.adapters.base.BaseFragmentResult
import com.smart.cleaner.phoneclean.ui_core.adapters.models.OptimizingType
import dagger.hilt.android.AndroidEntryPoint
import jamycake.lifecycle_aware.previousBackStackEntry
import kotlinx.coroutines.flow.collect
import yin_kio.garbage_clean.presentation.R
import yin_kio.garbage_clean.presentation.databinding.FragmentCleaningResultBinding
import yin_kio.garbage_clean.presentation.garbage_list.ViewModel
import javax.inject.Inject

@AndroidEntryPoint
class CleaningResultFragment : BaseFragmentResult(R.layout.fragment_cleaning_result) {

    private val binding: FragmentCleaningResultBinding by viewBinding()

    private val viewModel: ViewModel by previousBackStackEntry()

    @Inject
    lateinit var settings: Settings

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        setStyle(STYLE_NO_FRAME, general.R.style.Dialog)
        return super.onCreateDialog(savedInstanceState).apply {
            this.window?.setBackgroundDrawable(ColorDrawable(Color.WHITE))
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        settings.saveTimeJunkClean()

        binding.toolbar.binding.btnCrossExit.setOnClickListener {
            findNavController().navigateUp()
            viewModel.start()
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.state.collect {
                binding.size.text = it.freedSpace
            }
        }
    }

    override val typeResult: OptimizingType
        get() = OptimizingType.Clean

    override fun setRecyclerView(): RecyclerView {
        return binding.recycler
    }
}