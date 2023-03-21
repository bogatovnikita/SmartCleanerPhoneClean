package yin_kio.garbage_clean.domain.use_cases

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import yin_kio.garbage_clean.domain.entities.GarbageFiles
import yin_kio.garbage_clean.domain.entities.GarbageType
import yin_kio.garbage_clean.domain.out.Navigator
import yin_kio.garbage_clean.domain.out.Outer
import yin_kio.garbage_clean.domain.services.DeleteFormMapper
import kotlin.coroutines.CoroutineContext

internal class GarbageCleanerUseCasesImpl(
    private val garbageFiles: GarbageFiles,
    private val mapper: DeleteFormMapper,
    private val outer: Outer,
    private val coroutineScope: CoroutineScope,
    private val dispatcher: CoroutineContext,
    private val updater: Updater,
    private val deleteUseCase: DeleteUseCase
) : GarbageCleanUseCases {


    override fun switchSelectAll() = async {
        garbageFiles.deleteForm.switchSelectAll()
        updateDeleteForm()
    }
    override fun switchSelection(garbageType: GarbageType) = async {
        garbageFiles.deleteForm.switchSelection(garbageType)
        updateDeleteForm()
    }

    private fun updateDeleteForm() {
        val deleteFormOut = mapper.createDeleteFormOut(garbageFiles.deleteForm)
        outer.outDeleteForm(deleteFormOut)
    }

    override fun deleteIfCan(navigator: Navigator) = deleteUseCase.delete(navigator)

    override fun update(navigator: Navigator) = updater.update(navigator)

    private fun async(action: suspend () -> Unit){
        coroutineScope.launch(dispatcher) { action() }
    }

    override fun close(navigator: Navigator) {
        navigator.close()
    }
}