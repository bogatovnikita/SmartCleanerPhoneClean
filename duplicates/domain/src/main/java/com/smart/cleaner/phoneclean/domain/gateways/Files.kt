package com.smart.cleaner.phoneclean.domain.gateways

import com.smart.cleaner.phoneclean.domain.models.File
import com.smart.cleaner.phoneclean.domain.models.ImageInfo

interface Files {
    suspend fun getImages() : List<ImageInfo>
    suspend fun getFiles() : List<File>
    suspend fun delete(path: String)
    suspend fun copy(path: String, destination: String)
    fun folderForUnited() : String
}