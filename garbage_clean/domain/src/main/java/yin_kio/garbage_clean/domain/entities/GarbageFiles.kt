package yin_kio.garbage_clean.domain.entities

import java.io.File

internal class GarbageFiles : MutableMap<GarbageType, MutableSet<String>> by mutableMapOf() {


    private val apks = arrayOf(APK)
    private val temp = arrayOf(TMP, TEMP, TEMP_PATH, TMP_PATH)
    private val rest = arrayOf(DAT, LOG, LOG_PATH)
    private val thumb = arrayOf(THUMB, THUMB_PATH, THUMBNAILS_PATH)

    private var _deleteForm = DeleteForm()
    val deleteForm get() = _deleteForm

    fun setFiles(files: List<String>){
        clear()

        files.forEach {

            val file = File(it)
            if(file.isDirectory && file.list().isNullOrEmpty()) addTo(GarbageType.EmptyFolders, file.absolutePath)

            if(file.absolutePath.lowercase().containsMask(apks)) addTo(GarbageType.Apk, file.absolutePath)

            if(file.absolutePath.lowercase().containsMask(temp)) addTo(GarbageType.Temp, file.absolutePath)

            if(file.absolutePath.lowercase().containsMask(rest)) addTo(GarbageType.RestFiles, file.absolutePath)

            if(file.absolutePath.lowercase().containsMask(thumb)) addTo(GarbageType.Thumbnails, file.absolutePath)

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

    companion object{
        const val APK = ".apk"
        const val TMP = ".tmp"
        const val TEMP = ".temp"
        const val TEMP_PATH = "/temp/"
        const val TMP_PATH = "/tmp/"
        const val DAT = ".dat"
        const val LOG = ".log"
        const val LOG_PATH = "/log/"
        const val THUMB = ".thumb"
        const val THUMB_PATH = "/thumb/"
        const val THUMBNAILS_PATH = "/thumbnails/"
    }

}