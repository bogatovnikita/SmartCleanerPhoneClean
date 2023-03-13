package yin_kio.garbage_clean.presentation.view_model

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import yin_kio.garbage_clean.data.*
import yin_kio.garbage_clean.domain.GarbageCleanFactory
import yin_kio.garbage_clean.presentation.presenter.ScreenPresenter

class ScreenViewModelFactory  {

    fun create(applicationContext: Context, androidViewModel: ViewModel) : ScreenViewModel {
        androidViewModel.apply {
            val presenter = ScreenPresenter(
                context = applicationContext
            )
            val useCases = GarbageCleanFactory.createUseCases(
                files = FilesImpl(),
                outBoundary = presenter,
                coroutineScope = viewModelScope,
                fileSystemInfoProvider = FileSystemInfoProviderImpl(applicationContext),
                permissions = PermissionsImpl(applicationContext),
                ads = OlejaAds(applicationContext),
                noDeletableFiles = NoDeletableFilesImpl(applicationContext)
            )
            val viewModel = ScreenViewModel(useCases)

            presenter.viewModel = viewModel


            return viewModel
        }
    }

}