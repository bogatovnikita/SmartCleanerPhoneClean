package yin_kio.garbage_clean.presentation.garbage_list

import yin_kio.garbage_clean.domain.services.garbage_files.GarbageType
import yin_kio.garbage_clean.domain.ui_out.Garbage
import yin_kio.garbage_clean.domain.ui_out.UiOuter
import yin_kio.garbage_clean.domain.use_cases.UpdateState
import java.io.File

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

    override fun updateGroup(group: GarbageType, hasSelected: Boolean) {
        viewModel?.sendCommand(Command.UpdateGroup(group))
        updateButtonOpacity(hasSelected)
    }

    override fun showPermissionDialog() {
        viewModel?.sendCommand(Command.ShowPermissionDialog)
    }

    override fun outGarbage(garbage: List<Garbage>, wasClean: Boolean) {
        viewModel?.update { it.copy(
            size = presenter.presentSize(garbage.sumOf { it.files.sumOf { it.length() } }),
            buttonText = presenter.presentButtonText(true),
            garbageGroups = presenter.presentGarbage(garbage),
            isShowPermissionRequired = false,
            buttonOpacity = 0.5f,
            message = presenter.presentMessage(garbage),
            messageColor = presenter.presentProgressMessageColor(garbage, wasClean),
            sizeMessageColor = presenter.presentSizeMessageColor(garbage, wasClean),
            isExpandEnabled = garbage.isNotEmpty()
        ) }
    }

    override fun showUpdateProgress(wasClean: Boolean) {
        viewModel?.update { it.copy(
            size = presenter.persentProgressSize(),
            buttonText = presenter.presentButtonText(true),
            garbageGroups = presenter.presentGarbageForProgress(),
            isShowPermissionRequired = false,
            buttonOpacity = 0.5f,
            message = presenter.presentMessage(true),
            messageColor = presenter.presentProgressMessageColor(wasClean),
            sizeMessageColor = presenter.presentProgressSizeMessageColor(wasClean)
        ) }
    }

    override fun showPermissionRequired() {
        viewModel?.update { it.copy(
            size = presenter.presentUnknownSize(),
            buttonText = presenter.presentButtonText(false),
            garbageGroups = presenter.presentGarbageWithoutPermission(),
            isShowPermissionRequired = true,
            message = presenter.presentMessage(false),
            messageColor = presenter.presentNoPermissionMessageColor(),
            sizeMessageColor = presenter.presentNoPermissionSizeMessageColor()
        ) }
    }

    override fun showCleanProgress(files: List<File>) {
        viewModel?.update { it.copy(
            cleanMessages = presenter.presentCleanProgressMessages(files)
        ) }
        viewModel?.sendCommand(Command.ShowCleanProgress)
    }

    override fun showResult(result: Long) {
        viewModel?.update { it.copy(
            freedSpace = presenter.presentFreedSpace(result)
        ) }
        viewModel?.sendCommand(Command.ShowResult)
    }

    override fun updateChildrenAndGroup(group: GarbageType, hasSelected: Boolean) {
        viewModel?.sendCommand(Command.UpdateChildrenAndGroup(group))
        updateButtonOpacity(hasSelected)
    }

    private fun updateButtonOpacity(hasSelected: Boolean) {
        viewModel?.update {
            it.copy(
                buttonOpacity = presenter.presentButtonOpacity(hasSelected)
            )
        }
    }

    override fun removeCleanProgressItem() {
        viewModel?.update { it.copy(
            cleanMessages = it.cleanMessages
        ) }
    }

    override fun hidePermissionRequired() {
        viewModel?.update { it.copy(
            isShowPermissionRequired = false
        ) }
    }

    override fun updageLanguage(updateState: UpdateState, garbage: List<Garbage>, wasClean: Boolean) {
        presenter.updateLanguage()
        when(updateState){
            UpdateState.Error -> showPermissionRequired()
            UpdateState.Progress -> showUpdateProgress(wasClean)
            UpdateState.Successful -> outGarbage(garbage, wasClean)
        }
    }
}