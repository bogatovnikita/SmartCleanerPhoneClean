import android.os.Environment
import yin_kio.file_utils.FileUtilsImpl
import yin_kio.garbage_clean.domain.gateways.Files
import java.io.File

class FilesImpl : Files {

    private val fileUtils = FileUtilsImpl()

    override suspend fun getAllFiles(): List<File> {
        return fileUtils.getAllFiles(Environment.getExternalStorageDirectory())
    }

    override suspend fun deleteFiles(files: List<File>) {
        fileUtils.deleteFiles(files)
    }
}