package yin_kio.garbage_clean.domain.garbage_files

import yin_kio.garbage_clean.domain.entities.DeleteForm
import yin_kio.garbage_clean.domain.entities.FormItem
import yin_kio.garbage_clean.domain.garbage_files.ExtensionsGroups.apks
import yin_kio.garbage_clean.domain.garbage_files.ExtensionsGroups.rest
import yin_kio.garbage_clean.domain.garbage_files.ExtensionsGroups.temp
import yin_kio.garbage_clean.domain.garbage_files.ExtensionsGroups.thumb
import java.io.File

internal class GarbageFiles : MutableMap<GarbageType, MutableSet<String>> by mutableMapOf() {




    private var _deleteForm = DeleteForm()
    val deleteForm get() = _deleteForm

    fun setFiles(files: List<String>){
        clear()

        files.forEach {

            val file = File(it)
            val rawPath = file.absolutePath
            val lowercasePath = rawPath.lowercase()

            if(file.isDirectory && file.list().isNullOrEmpty()) addTo(GarbageType.EmptyFolders, rawPath)

            if(lowercasePath.containsMask(apks)) addTo(GarbageType.Apk, rawPath)

            if(lowercasePath.containsMask(temp)) addTo(GarbageType.Temp, rawPath)

            if(lowercasePath.containsMask(rest)) addTo(GarbageType.RestFiles, rawPath)

            if(lowercasePath.containsMask(thumb)) addTo(GarbageType.Thumbnails, rawPath)

        }

        updateDeleteForm()
    }

    private fun updateDeleteForm() {
        _deleteForm = DeleteForm()
        _deleteForm.addAll(createFromItems())
    }

    private fun createFromItems() : Collection<FormItem> {
        return map {
            FormItem(
                garbageType = it.key,
                size = it.value.sumOf { File(it).length() }
            )
        }
    }

    private fun String.containsMask(masks: Array<String>) : Boolean{
        masks.forEach {
            if (contains(it)) return true
        }
        return false
    }


    private fun addTo(garbageType: GarbageType, path: String){
        if (this[garbageType] == null){
            this[garbageType] = mutableSetOf(path)
        } else {
            this[garbageType]!!.add(path)
        }
    }


}