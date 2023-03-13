package yin_kio.garbage_clean.presentation.views

import android.app.Dialog
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.recycler_adapter.recyclerAdapter
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import yin_kio.garbage_clean.domain.entities.GarbageType
import yin_kio.garbage_clean.presentation.R
import yin_kio.garbage_clean.presentation.databinding.DialogGarbageCleanDeleteProgressBinding
import yin_kio.garbage_clean.presentation.databinding.ListOtemDeleteProgressBinding
import yin_kio.garbage_clean.presentation.presenter.ScreenItemsPresenter
import yin_kio.garbage_clean.presentation.view_model.parentViewModel

internal class DeleteProgressDialog : DialogFragment(R.layout.dialog_garbage_clean_delete_progress) {

    private val binding: DialogGarbageCleanDeleteProgressBinding by viewBinding()
    private val viewModel by lazy { parentViewModel() }
    private val presenter by lazy { ScreenItemsPresenter(requireContext()) }
    private val adapter by lazy { adapter() }
    private var deleteRequest = mutableListOf<GarbageType>() // нужно для запуска анимации, так как не хочется в домене хранить состояние анимации

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.recycler.adapter = adapter
        setupObserver()
        runAnimationAfterHalfSecond()
    }

    private fun setupObserver() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.state.collect {
                deleteRequest.addAll(it.deleteRequest)
                adapter.submitList(deleteRequest)
            }
        }
    }

    private fun runAnimationAfterHalfSecond() {
        viewLifecycleOwner.lifecycleScope.launch {
            delay(500)
            if (deleteRequest.isEmpty()) return@launch

            val size = deleteRequest.size
            for (i in 0..size) {
                delay(8000L / size)
                deleteRequest.removeFirstOrNull()
                adapter.notifyItemRemoved(0)
            }
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog = noCancelableDialog()

    override fun onStart() {
        super.onStart()
        setupLayoutParams()
    }

    private fun setupLayoutParams() {
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }


    private fun noCancelableDialog() = object : Dialog(requireContext()) {
        init {
            setCancelable(false)
            setCanceledOnTouchOutside(false)

            window?.setBackgroundDrawable(ColorDrawable(android.R.color.transparent))
        }

        override fun onBackPressed() {}
    }


    private fun adapter() = recyclerAdapter<GarbageType, ListOtemDeleteProgressBinding>(
        onBind = { item, _ ->
            root.text = presenter.presentName(item)
        }
    )

}