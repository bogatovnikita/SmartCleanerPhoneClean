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

    companion object{
        private val separator = File.separator

        const val APK = ".apk"
        const val TMP = ".tmp"
        const val TEMP = ".temp"
        val TEMP_PATH = "${separator}temp${separator}"
        val TMP_PATH = "${separator}tmp${separator}"
        const val DAT = ".dat"
        const val LOG = ".log"
        val LOG_PATH = "${separator}log${separator}"
        const val THUMB = ".thumb"
        val THUMB_PATH = "${separator}thumb${separator}"
        val THUMBNAILS_PATH = "${separator}thumbnails${separator}"
    }

}