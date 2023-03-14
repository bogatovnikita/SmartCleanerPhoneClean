package yin_kio.garbage_clean.domain.use_cases

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import yin_kio.garbage_clean.domain.services.DeleteFormMapper
import yin_kio.garbage_clean.domain.entities.FileSystemInfo
import yin_kio.garbage_clean.domain.entities.GarbageFiles
import yin_kio.garbage_clean.domain.gateways.FileSystemInfoProvider
import yin_kio.garbage_clean.domain.gateways.Files
import yin_kio.garbage_clean.domain.gateways.NoDeletableFiles
import yin_kio.garbage_clean.domain.gateways.Permissions
import yin_kio.garbage_clean.domain.out.DeleteFormOut
import yin_kio.garbage_clean.domain.out.DeleteProgressState
import yin_kio.garbage_clean.domain.out.Outer
import kotlin.coroutines.CoroutineContext

internal class UpdateUseCase(
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

    fun update() = async {
        if (permissions.hasStoragePermission){
            outAll()
        } else {
            outer.outHasPermission(false)
        }

    }

    private suspend fun outAll() {
        outer.outDeleteProgress(DeleteProgressState.Wait)
        outer.outHasPermission(true)
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