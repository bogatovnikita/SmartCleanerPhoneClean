import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import yin_kio.garbage_clean.domain.entities.garbage_files.GarbageFilesDistributor
import yin_kio.garbage_clean.domain.entities.garbage_files.GarbageType
import java.io.File

class GarbageDistributorTest {

    private val garbageFiles = GarbageFilesDistributor()

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



    @Test
    fun testDistribute(){

        val allFiles = (apk + temp + rest + thumb + emptyFolders + other).map { File(it) }
        val allGarbage = garbageFiles.distribute(allFiles)
        assertContainsAll(allGarbage)


    }



    private fun assertContainsAll(allGarbage: Map<GarbageType, List<File>>) {
        assertEquals(apk.toPathsSet(), allGarbage[GarbageType.Apk]!!)
        assertEquals(temp.toPathsSet(), allGarbage[GarbageType.Temp]!!)
        assertEquals(rest.toPathsSet(), allGarbage[GarbageType.RestFiles]!!)
        assertEquals(thumb.toPathsSet(), allGarbage[GarbageType.Thumbnails]!!)
        assertEquals(emptyFolders.toPathsSet(), allGarbage[GarbageType.EmptyFolders]!!)
    }
    private fun List<String>.toPathsSet() : List<File>{
        return map { File(it) }
    }

}