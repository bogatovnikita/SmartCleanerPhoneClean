package yin_kio.garbage_clean.presentation.view_model

import androidx.fragment.app.Fragment
import yin_kio.garbage_clean.presentation.views.GarbageCleanParentFragment

fun Fragment.parentViewModel() = (parentFragment?.parentFragment as GarbageCleanParentFragment).viewModel