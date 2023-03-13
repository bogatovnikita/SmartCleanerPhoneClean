package entities

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import yin_kio.garbage_clean.domain.entities.GarbageFiles
import yin_kio.garbage_clean.domain.entities.GarbageType
import java.io.File

class GarbageFilesTest {

    private lateinit var garbageFiles: GarbageFiles

    private val apk = listOf(
        "app.apk",
    )
    private val temp = listOf(
        "temp_file.temp",
        "temp_file.tmp",
        "/temp/temp_file",
        "/tmp/temp_file",
    )

    private val rest = listOf(
        "log.log",
        "/log/logfile",
        "dat.dat",
    )

    private val thumb = listOf(
        "thumb.thumb",
        "/thumb/thumbnail",
        "/thumbnails/thumbnail"
    )

    private val other = listOf(
        "some other file 1",
        "some other file 2",
        "some other file 3",
        "temporary_need_file"
    )

    private val emptyFolders = listOf("src/test/resources/empty_folder")

    @BeforeEach
    fun setup(){
        garbageFiles = GarbageFiles()
    }


    @Test
    fun testSetFiles(){
        val oldDeleteForm = garbageFiles.deleteForm

        val input = apk + temp + rest + thumb + emptyFolders + other
        garbageFiles.setFiles(input)
        assertContainsAll()
        assertTrue(oldDeleteForm !== garbageFiles.deleteForm)
        assertTrue(garbageFiles.deleteForm.isNotEmpty())

        garbageFiles.setFiles(apk + temp)
        assertContainsApkAndTemp()

    }

    private fun assertContainsApkAndTemp(){
        assertEquals(garbageFiles[GarbageType.Apk]!!, apk.toPathsSet())
        assertEquals(garbageFiles[GarbageType.Temp]!!, temp.toPathsSet())
        assertNull(garbageFiles[GarbageType.RestFiles])
        assertNull(garbageFiles[GarbageType.Thumbnails])
        assertNull(garbageFiles[GarbageType.EmptyFolders])
    }

    private fun assertContainsAll() {
        assertEquals(garbageFiles[GarbageType.Apk]!!, apk.toPathsSet())
        assertEquals(garbageFiles[GarbageType.Temp]!!, temp.toPathsSet())
        assertEquals(garbageFiles[GarbageType.RestFiles]!!, rest.toPathsSet())
        assertEquals(garbageFiles[GarbageType.Thumbnails]!!, thumb.toPathsSet())
        assertEquals(garbageFiles[GarbageType.EmptyFolders]!!, emptyFolders.toPathsSet())
    }
    private fun List<String>.toPathsSet() : Set<String>{
        return map { File(it).absolutePath }.toSet()
    }

}