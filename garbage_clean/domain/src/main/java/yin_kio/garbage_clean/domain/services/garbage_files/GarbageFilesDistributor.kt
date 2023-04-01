package yin_kio.garbage_clean.domain.services.garbage_files

import java.io.File

internal class GarbageFilesDistributor {

    fun distribute(files: List<File>) : Map<GarbageType, List<File>>{
        val distributed = mutableMapOf<GarbageType, List<File>>()

        files.forEach {file ->
            val rawPath = file.absolutePath
            val lowercasePath = rawPath.lowercase()

            if(file.isDirectory && file.list().isNullOrEmpty()) distributed.addTo(GarbageType.EmptyFolders, file)
            if(lowercasePath.containsMask(ExtensionsGroups.apks)) distributed.addTo(GarbageType.Apk, file)
            if(lowercasePath.containsMask(ExtensionsGroups.temp)) distributed.addTo(GarbageType.Temp, file)
            if(lowercasePath.containsMask(ExtensionsGroups.rest)) distributed.addTo(GarbageType.RestFiles, file)
            if(lowercasePath.containsMask(ExtensionsGroups.thumb)) distributed.addTo(GarbageType.Thumbnails, file)
        }

        return distributed
    }

    private fun String.containsMask(masks: Array<String>) : Boolean{
        masks.forEach {
            if (contains(it)) return true
        }
        return false
    }

    private fun MutableMap<GarbageType, List<File>>.addTo(garbageType: GarbageType, file: File){
        if (this[garbageType] == null){
            this[garbageType] = mutableListOf(file)
        } else {
            (this[garbageType]!! as MutableList).add(file)
        }
    }

}