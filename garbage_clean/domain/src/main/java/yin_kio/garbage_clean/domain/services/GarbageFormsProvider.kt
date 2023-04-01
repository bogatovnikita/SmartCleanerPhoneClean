package yin_kio.garbage_clean.domain.services

import yin_kio.garbage_clean.domain.services.garbage_files.GarbageType
import yin_kio.garbage_clean.domain.services.selectable_form.SelectableForm
import java.io.File

interface GarbageFormsProvider {

    fun provide() : Map<GarbageType, SelectableForm<File>>

}