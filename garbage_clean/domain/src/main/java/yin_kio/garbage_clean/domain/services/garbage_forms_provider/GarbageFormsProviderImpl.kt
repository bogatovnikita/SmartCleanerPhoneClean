package yin_kio.garbage_clean.domain.services.garbage_forms_provider

import yin_kio.garbage_clean.domain.gateways.Files
import yin_kio.garbage_clean.domain.services.garbage_files.GarbageFilesDistributor
import yin_kio.garbage_clean.domain.services.garbage_files.GarbageType
import yin_kio.garbage_clean.domain.services.selectable_form.SelectableForm
import yin_kio.garbage_clean.domain.services.selectable_form.SimpleSelectableForm
import java.io.File

 internal class GarbageFormsProviderImpl(
    private val files: Files
) : GarbageFormsProvider {

    private val distributor = GarbageFilesDistributor()

    override suspend fun provide(): Map<GarbageType, SelectableForm<File>> {
        return distributor.distribute(files.getAllFiles()).map {
            val key = it.key
            val form = SimpleSelectableForm<File>()
            form.content = it.value
            Pair(key, form)
        }.toMap()
    }
}