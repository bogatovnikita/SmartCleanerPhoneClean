package yin_kio.garbage_clean.domain.ui_out.garbage_out_creator

import yin_kio.garbage_clean.domain.services.garbage_files.GarbageType
import yin_kio.garbage_clean.domain.services.selectable_form.SelectableForm
import yin_kio.garbage_clean.domain.ui_out.Garbage
import java.io.File

internal interface GarbageOutCreator {

    fun create(forms: Map<GarbageType, SelectableForm<File>>) : List<Garbage>

}