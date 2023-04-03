package yin_kio.garbage_clean.presentation

import android.util.Log
import yin_kio.garbage_clean.domain.services.garbage_files.GarbageType
import yin_kio.garbage_clean.domain.ui_out.Garbage
import yin_kio.garbage_clean.domain.ui_out.UiOuter

class UIOuterImpl(
    private val presenter: Presenter
) : UiOuter {


    var viewModel: ViewModel? = null

    override fun closePermissionDialog() {
//        TODO("Not yet implemented")
    }

    override fun requestPermission() {
//        TODO("Not yet implemented")
    }

    override fun updateGroup(group: GarbageType) {
//        TODO("Not yet implemented")
    }

    override fun showPermissionDialog() {
        viewModel?.sendCommand(Command.ShowDialog)
    }

    override fun outGarbage(garbage: List<Garbage>) {
//        TODO("Not yet implemented")
    }

    override fun showUpdateProgress() {
//        TODO("Not yet implemented")
    }

    override fun showPermissionRequired() {
        viewModel?.update { it.copy(
            size = presenter.presentUnknownSize(),
            buttonText = presenter.presentButtonText(false),
            garbage = presenter.presentGarbageWithoutPermission()
        ) }
    }

    override fun showCleanProgress(messages: List<String>) {
//        TODO("Not yet implemented")
    }

    override fun showResult(result: Long) {
//        TODO("Not yet implemented")
    }
}