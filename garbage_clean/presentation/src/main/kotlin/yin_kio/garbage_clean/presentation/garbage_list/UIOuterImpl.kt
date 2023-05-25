package yin_kio.garbage_clean.presentation.garbage_list

import yin_kio.garbage_clean.domain.services.garbage_files.GarbageType
import yin_kio.garbage_clean.domain.ui_out.Garbage
import yin_kio.garbage_clean.domain.ui_out.UiOut
import yin_kio.garbage_clean.domain.ui_out.UiOuter
import java.io.File

class UIOuterImpl(
    private val presenter: Presenter,
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

    override fun changeLanguage(uiOut: UiOut) {
        presenter.updateLanguage()
        out(uiOut)
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



    override fun showAttentionMessagesColors() {
        viewModel?.update { it.copy(
            messageColor = presenter.presentNoPermissionMessageColor(),
            sizeMessageColor = presenter.presentNoPermissionSizeMessageColor()
        ) }

    }



    override fun out(uiOut: UiOut) {
        when(uiOut){
            UiOut.Init -> {}
            UiOut.StartWithoutPermission -> showStartWithoutPermission()
            is UiOut.UpdateProgress -> showUpdateProgress(uiOut.isCleaned)
            is UiOut.Updated -> outGarbage(uiOut.garbageOuts, uiOut.isCleaned)
        }
    }



    private fun showUpdateProgress(isCleaned: Boolean) {
        viewModel?.update { presenter.presentUpdateProgressScreen(it, isCleaned) }
    }


    private fun showStartWithoutPermission(){
        viewModel?.update { presenter.presentStartWithoutPermissionScreen() }
        viewModel?.sendCommand(Command.ShowPermissionDialog)
    }


    private fun outGarbage(garbage: List<Garbage>, isCleaned: Boolean) {
        viewModel?.update { presenter.presentUpdatedScreen(it, garbage, isCleaned)}
    }





}