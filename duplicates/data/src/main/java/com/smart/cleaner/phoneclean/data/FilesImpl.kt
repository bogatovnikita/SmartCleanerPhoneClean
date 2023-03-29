package com.smart.cleaner.phoneclean.data

import android.os.Environment
import com.smart.cleaner.phoneclean.domain.gateways.Files
import com.smart.cleaner.phoneclean.domain.models.ImageInfo
import yin_kio.file_utils.FileGroups
import yin_kio.file_utils.FileUtils
import java.io.File
import javax.inject.Inject

class FilesImpl @Inject constructor(
    private val fileUtils: FileUtils
) : Files {

    override suspend fun getImages(): List<ImageInfo> {
        val fileGroups = FileGroups()
        return fileUtils.getAllFiles(Environment.getExternalStorageDirectory()).filter {
            fileGroups.images[it.extension.uppercase()] != null
        }.map { ImageInfo(path = it.absolutePath, size = it.length())  }
    }

    override suspend fun getFiles(): List<com.smart.cleaner.phoneclean.domain.models.File> {
        val fileGroups = FileGroups()
        return fileUtils.getAllFiles(Environment.getExternalStorageDirectory()).filter {
            fileGroups.documents[it.extension.uppercase()] != null
        }.map { com.smart.cleaner.phoneclean.domain.models.File(path = it.absolutePath, size = it.length()) }
    }

    override suspend fun delete(path: String) {
        fileUtils.deleteFile(path)
    }

    override suspend fun copy(path: String, destination: String) {
        fileUtils.copyFile(File(path), File(destination))
    }

    override fun folderForUnited(): String {
        return Environment.getExternalStorageDirectory().absolutePath + "/" + Environment.DIRECTORY_DCIM + "/"
    }

}