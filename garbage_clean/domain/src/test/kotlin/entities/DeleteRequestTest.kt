package entities

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import yin_kio.garbage_clean.domain.entities.DeleteRequest
import yin_kio.garbage_clean.domain.entities.GarbageType

class DeleteRequestTest {


    private lateinit var deleteForm: DeleteRequest

    @BeforeEach
    fun setup(){
        deleteForm = DeleteRequest()
    }


    @Test
    fun `add and remove`(){
        deleteForm.add(GarbageType.Apk)
        deleteForm.add(GarbageType.Apk)

        assertTrue(deleteForm.contains(GarbageType.Apk))
        assertEquals(1, deleteForm.count { true })

        deleteForm.remove(GarbageType.Apk)
        assertFalse(deleteForm.contains(GarbageType.Apk))
    }

}