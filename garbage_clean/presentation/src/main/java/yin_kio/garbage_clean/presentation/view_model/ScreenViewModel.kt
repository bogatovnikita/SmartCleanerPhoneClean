package yin_kio.garbage_clean.presentation.view_model

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import yin_kio.garbage_clean.domain.entities.GarbageType
import yin_kio.garbage_clean.domain.out.DeleteProgressState
import yin_kio.garbage_clean.domain.use_cases.GarbageCleanUseCases
import yin_kio.garbage_clean.presentation.models.ScreenState
import yin_kio.garbage_clean.presentation.models.UiDeleteFromItem
import yin_kio.garbage_clean.presentation.models.UiFileSystemInfo

class ScreenViewModel(
    private val useCases: GarbageCleanUseCases
) : MutableScreenViewModel,
    ObservableScreenViewModel,
    GarbageCleanUseCases by useCases {


    private val _state = MutableStateFlow(ScreenState())
    override val state = _state.asStateFlow()

    init {
        update()
    }

    override fun setFileSystemInfo(uiFileSystemInfo: UiFileSystemInfo) {
        _state.value = state.value.copy(
            fileSystemInfo = uiFileSystemInfo
        )
    }

    override fun setIsInProgress(isInProgress: Boolean) {
        _state.value = state.value.copy(isInProgress = isInProgress)
    }

    override fun setHasPermission(hasPermission: Boolean) {
        _state.value = state.value.copy(hasPermission = hasPermission)
    }

    override fun setDeleteProgress(deleteProgressState: DeleteProgressState) {
        _state.value = state.value.copy(deleteProgressState = deleteProgressState)
    }

    override fun setDeleteFormItems(deleteFormItems: List<UiDeleteFromItem>) {
        _state.value = state.value.copy(deleteFormItems = deleteFormItems)
    }

    override fun setIsAllSelected(isAllSelected: Boolean) {
        _state.value = state.value.copy(isAllSelected = isAllSelected)
    }

    override fun setCanFreeSize(canFreeVolume: String) {
        _state.value = state.value.copy(canFreeVolume = canFreeVolume)
    }

    override fun setButtonText(text: String) {
        _state.value = state.value.copy(buttonText = text)
    }

    override fun setButtonBgRes(bgRes: Int) {
        _state.value = state.value.copy(buttonBgRes = bgRes)
    }

    override fun setDeleteRequest(deleteRequest: List<GarbageType>) {
        _state.value = state.value.copy(deleteRequest = deleteRequest)
    }

    override fun setIsClosed(isClosed: Boolean) {
        _state.value = state.value.copy(isClosed = isClosed)
    }

    override fun setDeletedSize(size: String) {
        _state.value = state.value.copy(freed = size)
    }
}