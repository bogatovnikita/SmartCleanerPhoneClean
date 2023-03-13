package yin_kio.garbage_clean.presentation.view_model

import kotlinx.coroutines.flow.StateFlow
import yin_kio.garbage_clean.domain.use_cases.GarbageCleanUseCases
import yin_kio.garbage_clean.presentation.models.ScreenState

interface ObservableScreenViewModel : GarbageCleanUseCases {
    val state: StateFlow<ScreenState>
}