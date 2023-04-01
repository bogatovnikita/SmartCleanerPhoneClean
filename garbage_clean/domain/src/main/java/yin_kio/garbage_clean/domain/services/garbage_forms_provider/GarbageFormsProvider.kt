package yin_kio.garbage_clean.domain.services.garbage_forms_provider

import yin_kio.garbage_clean.domain.services.garbage_files.GarbageType
import yin_kio.garbage_clean.domain.services.selectable_form.SelectableForm
import java.io.File

internal interface GarbageFormsProvider {

    suspend fun provide() : Map<GarbageType, SelectableForm<File>>

}