package yin_kio.garbage_clean.domain.use_cases

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import yin_kio.garbage_clean.domain.garbage_files.GarbageFiles
import yin_kio.garbage_clean.domain.gateways.Ads
import yin_kio.garbage_clean.domain.gateways.Files
import yin_kio.garbage_clean.domain.gateways.NoDeletableFiles
import yin_kio.garbage_clean.domain.out.Navigator
import yin_kio.garbage_clean.domain.out.Outer
import yin_kio.garbage_clean.domain.services.DeleteRequestInterpreter
import java.io.File
import kotlin.coroutines.CoroutineContext

internal class DeleteUseCase(
    private val garbageFiles: GarbageFiles,
    private val ads: Ads,
    private val coroutineScope: CoroutineScope,
    private val dispatcher: CoroutineContext,
    private val files: Files,
    private val noDeletableFiles: NoDeletableFiles,
    private val outer: Outer
) {

    private val interpreter = DeleteRequestInterpreter(garbageFiles)

    fun delete(navigator: Navigator) = async {
        val deleteRequest = garbageFiles.deleteForm.deleteRequest

        if (deleteRequest.isEmpty()) return@async

        ads.preloadAd()
        navigator.showDeleteProgress()
        outer.outDeleteRequest(deleteRequest.toList())

        val interpretedRequest = interpreter.interpret(deleteRequest)
        val noDeletable = files.deleteAndGetNoDeletable(interpretedRequest)

        val deleted = interpretedRequest.filter { !noDeletable.contains(it) }
        val deletedSize = deleted.sumOf { File(it).length() }

        noDeletableFiles.save(noDeletable)
        delay(8000)
        outer.outDeletedSize(deletedSize)
        navigator.complete()

    }

    private fun async(action: suspend () -> Unit){
        coroutineScope.launch(dispatcher) { action() }
    }

}