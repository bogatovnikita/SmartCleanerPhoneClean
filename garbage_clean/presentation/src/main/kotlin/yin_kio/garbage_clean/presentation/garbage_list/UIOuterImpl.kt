package yin_kio.garbage_clean.presentation.garbage_list

import android.util.Log
import yin_kio.garbage_clean.domain.services.garbage_files.GarbageType
import yin_kio.garbage_clean.domain.ui_out.Garbage
import yin_kio.garbage_clean.domain.ui_out.UiOuter
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
            buttonOpacity = 0.5f,
            message = presenter.presentMessage(true),
            messageColor = presenter.presentMessageColor(true)
        ) }
    }

    override fun showPermissionRequired() {
        viewModel?.update { it.copy(
            size = presenter.presentUnknownSize(),
            buttonText = presenter.presentButtonText(false),
            garbage = presenter.presentGarbageWithoutPermission(),
            isShowPermissionRequired = true,
            message = presenter.presentMessage(false),
            messageColor = presenter.presentMessageColor(false)
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
}