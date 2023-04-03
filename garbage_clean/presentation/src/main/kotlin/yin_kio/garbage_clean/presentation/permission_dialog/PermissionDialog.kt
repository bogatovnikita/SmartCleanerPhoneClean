package yin_kio.garbage_clean.presentation.permission_dialog

import android.app.Dialog
import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import by.kirich1409.viewbindingdelegate.viewBinding
import jamycake.lifecycle_aware.previousBackStackEntry
import yin_kio.garbage_clean.presentation.R
import yin_kio.garbage_clean.presentation.databinding.DialogPermissionBinding
import yin_kio.garbage_clean.presentation.garbage_list.ViewModel

class PermissionDialog : DialogFragment(R.layout.dialog_permission) {

    private val binding: DialogPermissionBinding by viewBinding()
    private val viewModel: ViewModel by previousBackStackEntry()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.cancel.setOnClickListener { viewModel.closePermissionDialog() }
        binding.close.setOnClickListener { viewModel.closePermissionDialog() }
        binding.ok.setOnClickListener { viewModel.requestPermission() }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState).apply {
            window?.setBackgroundDrawableResource(R.drawable.bg_dialog)
        }
    }

}