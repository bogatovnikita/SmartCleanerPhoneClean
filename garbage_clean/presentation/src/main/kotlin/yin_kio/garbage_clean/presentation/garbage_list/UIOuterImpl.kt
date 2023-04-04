package yin_kio.garbage_clean.presentation.garbage_list

import yin_kio.garbage_clean.domain.services.garbage_files.GarbageType
import yin_kio.garbage_clean.domain.ui_out.Garbage
import yin_kio.garbage_clean.domain.ui_out.UiOuter

class UIOuterImpl(
    private val presenter: Presenter
) : UiOuter {


    var viewModel: ViewModel? = null

    override fun closePermissionDialog() {
        viewModel?.sendCommand(Command.ClosePermissionDialog)
    }

    override fun requestPermission() {
        viewModel?.sendCommand(Command.RequestPermission)
    }

    override fun updateGroup(group: GarbageType) {
        viewModel?.sendCommand(Command.UpdateGroup(group))
    }

    override fun showPermissionDialog() {
        viewModel?.sendCommand(Command.ShowPermissionDialog)
    }

    override fun outGarbage(garbage: List<Garbage>) {
        viewModel?.update { it.copy(
            size = presenter.presentSize(garbage.sumOf { it.files.sumOf { it.length() } }),
            buttonText = presenter.presentButtonText(true),
            garbage = presenter.presentGarbage(garbage),
            isShowPermissionRequired = false,
            buttonOpacity = 0.5f
        ) }
    }

    override fun showUpdateProgress() {
        viewModel?.update { it.copy(
            size = presenter.presentProgressSize(),
            buttonText = presenter.presentButtonText(true),
            garbage = presenter.presentGarbageForProgress(),
            isShowPermissionRequired = false,
            buttonOpacity = 0.5f
        ) }
    }

    override fun showPermissionRequired() {
        viewModel?.update { it.copy(
            size = presenter.presentUnknownSize(),
            buttonText = presenter.presentButtonText(false),
            garbage = presenter.presentGarbageWithoutPermission(),
            isShowPermissionRequired = true
        ) }
    }

    override fun showCleanProgress(messages: List<String>) {
//        TODO("Not yet implemented")
    }

    override fun showResult(result: Long) {
//        TODO("Not yet implemented")
    }

    override fun updateChildrenAndGroup(group: GarbageType) {
        viewModel?.sendCommand(Command.UpdateChildrenAndGroup(group))
    }
}