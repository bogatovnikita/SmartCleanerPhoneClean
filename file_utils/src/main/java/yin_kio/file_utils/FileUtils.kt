package yin_kio.file_utils

import java.io.File

interface FileUtils {

    suspend fun getAllFiles(folder: File) : List<File>
    suspend fun getAllFilesAndFolders(folder: File) : List<File>
    suspend fun copyFiles(files: List<File>, destination: File)
    suspend fun moveFiles(files: List<File>, destination: File)
    suspend fun deleteFiles(files: List<File>) : Long
    suspend fun deleteFile(path: String) : Long
    fun copyFile(file: File, destination: File)

}

