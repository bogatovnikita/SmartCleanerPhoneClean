package yin_kio.garbage_clean.domain.services

import yin_kio.garbage_clean.domain.services.garbage_files.GarbageType
import yin_kio.garbage_clean.domain.services.selectable_form.SelectableForm
import java.io.File

interface GarbageFormsCreator {

    fun create(files: List<File>) : Map<GarbageType, SelectableForm<File>>

}