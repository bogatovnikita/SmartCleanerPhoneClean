package yin_kio.garbage_clean.data

import android.os.Environment
import yin_kio.file_utils.FileUtilsImpl
import yin_kio.garbage_clean.domain.gateways.Files
import java.io.File

class FilesImpl : Files {

    private val fileUtils = FileUtilsImpl()

    override suspend fun deleteAndGetNoDeletable(paths: List<String>) : List<String>{
        val files = paths.map { File(it) }
        fileUtils.deleteFiles(files)
        return files.filter { it.exists() }.map { it.absolutePath }
    }

    override suspend fun getAll(): List<String> {
        return fileUtils.getAllFilesAndFolders(Environment.getExternalStorageDirectory()).map { it.absolutePath }
    }
}