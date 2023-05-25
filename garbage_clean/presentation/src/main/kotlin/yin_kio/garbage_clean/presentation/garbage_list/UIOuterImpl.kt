package yin_kio.garbage_clean.presentation.garbage_list

import android.content.Context
import android.text.format.Formatter.formatFileSize
import com.bogatovnikita.language_dialog.language.Language
import yin_kio.garbage_clean.domain.services.garbage_files.GarbageType
import yin_kio.garbage_clean.domain.ui_out.Garbage
import yin_kio.garbage_clean.domain.ui_out.UiOut
import yin_kio.garbage_clean.domain.ui_out.UiOuter
import yin_kio.garbage_clean.domain.use_case.UpdateState
import yin_kio.garbage_clean.presentation.R
import yin_kio.garbage_clean.presentation.garbage_list.adapter.models.GarbageGroup
import java.io.File

class UIOuterImpl(
    private val presenter: Presenter,
    context: Context
) : UiOuter {

    private val language = Language(context)
    private var context = language.changeLanguage()


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
            sizeText = presenter.presentSize(garbage.sumOf { it.files.sumOf { it.length() } }),
            buttonText = presenter.presentButtonText(true),
            garbageGroups = presenter.presentGarbage(garbage),
            isShowPermissionRequired = false,
            buttonOpacity = 0.5f,
            message = presenter.presentMessage(garbage),
            messageColor = presenter.presentProgressMessageColor(garbage, wasClean),
            sizeMessageColor = presenter.presentSizeMessageColor(garbage, wasClean),
            isExpandEnabled = garbage.isNotEmpty(),
            isInfoVisible = true
        ) }
    }

    override fun showUpdateProgress(wasClean: Boolean) {

        viewModel?.update { it.copy(
            sizeText = presenter.persentProgressSize(),
            buttonText = presenter.presentButtonText(true),
            garbageGroups = presenter.presentGarbageForProgress(),
            isShowPermissionRequired = false,
            buttonOpacity = 0.5f,
            message = presenter.presentMessage(true),
            messageColor = presenter.presentProgressMessageColor(wasClean),
            sizeMessageColor = presenter.presentProgressSizeMessageColor(wasClean),
            isInfoVisible = true
        ) }
    }

    override fun showPermissionRequired() {
        viewModel?.update { it.copy(
            sizeText = presenter.presentUnknownSize(),
            buttonText = presenter.presentButtonText(false),
            garbageGroups = presenter.presentGarbageWithoutPermission(),
            isShowPermissionRequired = true,
            message = presenter.presentMessage(false),
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
            UpdateState.Error -> {showPermissionRequired()}
            UpdateState.Progress -> showUpdateProgress(wasClean)
            UpdateState.Successful -> outGarbage(garbage, wasClean)
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

    override fun changeLanguage(uiOut: UiOut) {
        context = language.changeLanguage()
        presenter.updateLanguage()
        out(uiOut)
    }

    private fun showStartWithoutPermission(){
        viewModel?.update { it.copy(
            sizeText = formatFileSize(context, 0L).replace("0", "(?)"),
            buttonText = context.getString(R.string.junk_clean_scan),
            garbageGroups = GarbageType.values().map {
                GarbageGroup(
                    type = it,
                    name = presentGarbageName(it),
                    description = ""
                )
            },
            isShowPermissionRequired = true,
            buttonOpacity = 1f,
            message = context.getString(R.string.junk_clean_amount_message),
            sizeMessageColor = context.getColor(general.R.color.error),
            messageColor = context.getColor(general.R.color.primary),
            isExpandEnabled = false,
            isInfoVisible = true
        ) }

        viewModel?.sendCommand(Command.ShowPermissionDialog)
    }


    private fun presentGarbageName(garbageType: GarbageType) : String{

        val resId = when(garbageType){
            GarbageType.Apk -> R.string.junk_clean_apk
            GarbageType.Temp -> R.string.junk_clean_temp_files
            GarbageType.RestFiles -> R.string.junk_clean_residual_files
            GarbageType.EmptyFolders -> R.string.junk_clean_empty_filders
            GarbageType.Thumbnails -> R.string.junk_clean_miniatures
        }

        return context.getString(resId)
    }






}