package yin_kio.garbage_clean.domain.ui_out

import yin_kio.garbage_clean.domain.services.garbage_files.GarbageType
import yin_kio.garbage_clean.domain.services.selectable_form.SelectableForm
import java.io.File

class GarbageOutCreatorImpl : GarbageOutCreator {

    override fun create(forms: Map<GarbageType, SelectableForm<File>>): List<Garbage> {
        return forms.map {
            Garbage(
                type = it.key,
                files = it.value.content.toList()
            )
        }
    }
}