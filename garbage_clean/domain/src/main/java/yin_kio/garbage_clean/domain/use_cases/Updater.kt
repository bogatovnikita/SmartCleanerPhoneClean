package yin_kio.garbage_clean.domain.use_cases

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import yin_kio.garbage_clean.domain.entities.FileSystemInfo
import yin_kio.garbage_clean.domain.garbage_files.GarbageFiles
import yin_kio.garbage_clean.domain.gateways.FileSystemInfoProvider
import yin_kio.garbage_clean.domain.gateways.Files
import yin_kio.garbage_clean.domain.gateways.NoDeletableFiles
import yin_kio.garbage_clean.domain.gateways.Permissions
import yin_kio.garbage_clean.domain.out.DeleteFormOut
import yin_kio.garbage_clean.domain.out.Navigator
import yin_kio.garbage_clean.domain.out.Outer
import yin_kio.garbage_clean.domain.services.DeleteFormMapper
import kotlin.coroutines.CoroutineContext

internal class Updater(
    private val outer: Outer,
    private val coroutineScope: CoroutineScope,
    private val dispatcher: CoroutineContext,
    private val mapper: DeleteFormMapper,
    private val garbageFiles: GarbageFiles,
    private val files: Files,
    private val fileSystemInfoProvider: FileSystemInfoProvider,
    private val permissions: Permissions,
    private val noDeletableFiles: NoDeletableFiles
) {

    fun update(navigator: Navigator) = async {
        if (permissions.hasStoragePermission){
            outAll()
        } else {
            navigator.showPermission()
        }

    }

    private suspend fun outAll() {
        outer.outUpdateProgress(true)
        outer.outFileSystemInfo(getFileSystemInfo())

        loadFilesToEntityExceptNoDeletable()

        if (garbageFiles.isNotEmpty()) garbageFiles.deleteForm.switchSelectAll()

        outer.outDeleteForm(getDeleteFormOut())
        outer.outUpdateProgress(false)
    }

    private suspend fun loadFilesToEntityExceptNoDeletable() {
        val noDeletable = noDeletableFiles.get()
        garbageFiles.setFiles(files.getAll().filter { !noDeletable.contains(it) })
    }

    private suspend fun getFileSystemInfo() : FileSystemInfo{
        return fileSystemInfoProvider.getFileSystemInfo()
    }

    private fun getDeleteFormOut() : DeleteFormOut{
        return mapper.createDeleteFormOut(garbageFiles.deleteForm)
    }


    private fun async(action: suspend () -> Unit){
        coroutineScope.launch(dispatcher) { action() }
    }

}