package yin_kio.file_utils

import java.io.*

class FileUtilsImpl : FileUtils {

    override suspend fun getAllFiles(folder: File): List<File> {


        if (!folder.exists()) return listOf()
        val files = folder.listFiles() ?: return listOf()

        return getFilesRecursively(files)
    }

    private fun getFilesRecursively(files: Array<File>) : List<File> {
        val tempFiles = mutableListOf<File>()

        files.forEach { file ->
            if (file.isDirectory){
                file.listFiles()?.let {
                    tempFiles.addAll(getFilesRecursively(it))
                }
            } else {
                tempFiles.add(file)
            }
        }
        return tempFiles
    }

    override suspend fun getAllFilesAndFolders(folder: File): List<File> {
        if (!folder.exists()) return listOf()
        val files = folder.listFiles() ?: return listOf()

        return getFilesAndFoldersRecursively(files)
    }

    private fun getFilesAndFoldersRecursively(files: Array<File>) : List<File> {
        val res = mutableListOf<File>()

        files.forEach { file ->
            if (file.isDirectory){
                file.listFiles()?.let {
                    res.add(file)
                    res.addAll(getFilesRecursively(it))
                }
            } else {
                res.add(file)
            }
        }
        return res
    }



    override suspend fun copyFiles(files: List<File>, destination: File) {
        files.forEach { copyFile(it, destination) }
    }



    override suspend fun moveFiles(files: List<File>, destination: File) {
        copyFiles(files, destination)
        deleteFiles(files)
    }

    override fun copyFile(file: File, destination: File) {
        if (destination.isFile) throw IOException("Destination is not folder: ${destination.absolutePath}")

        var inputStream: InputStream? = null
        var outputStream: OutputStream? = null
        try {
            inputStream = FileInputStream(file)
            outputStream = FileOutputStream(copiedFile(destination, file))
            val buffer = ByteArray(1024)
            var length: Int
            while (inputStream.read(buffer).also { length = it } > 0) {
                outputStream.write(buffer, 0, length)
            }
        } finally {
            inputStream?.close()
            outputStream?.close()
        }
    }

    private fun copiedFile(destination: File, file: File): File {
        var copiedFile = File(destination.absolutePath + "/${file.name}")
        if (!copiedFile.exists()) return copiedFile

        var index = 1
        while (copiedFile.exists()) {
            val tail = file.name.replaceBeforeLast(".", "($index)")
            val head = file.name.substringBeforeLast('.') + " "
            val fileName = head + tail
            copiedFile = File(destination.absolutePath + "/${fileName}")
            index++
        }
        return copiedFile
    }

    override suspend fun deleteFiles(files: List<File>) : Long {
        var size = 0L
        files.forEach { size += deleteFile(it) }
        return size
    }

    override suspend fun deleteFile(path: String): Long {
        return deleteFile(File(path))

    }

    private fun deleteFile(file: File) : Long{
        return try {
            val length = file.length()

            if (file.delete()){
                length
            } else {
                0
            }
        } catch (ex: Exception){
            0
        }
    }
}