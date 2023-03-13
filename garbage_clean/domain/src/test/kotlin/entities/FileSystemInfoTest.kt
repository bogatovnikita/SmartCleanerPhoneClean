package entities

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Test
import yin_kio.garbage_clean.domain.entities.FileSystemInfo

class FileSystemInfoTest {

    @Test
    fun testEquals(){
        val info1 = FileSystemInfo(0,0,0)
        val info2 = FileSystemInfo(0,0,0)
        val info3 = FileSystemInfo(0,0,1)

        assertEquals(info1, info2)
        assertNotEquals(info1, info3)
    }

}