package yin_kio.garbage_clean.presentation.views

import yin_kio.garbage_clean.presentation.view_model.parentViewModel
import yin_kio.garbage_clean.presentation.view_model.ObservableScreenViewModel
import yin_kio.permissions_views.PermissionFragment

internal class GarbageCleanPermissionFragment : PermissionFragment<ObservableScreenViewModel>() {

    override fun provideViewModel(): ObservableScreenViewModel = parentViewModel()

    override fun actionOnResume(viewModel: ObservableScreenViewModel) = viewModel.update()
}