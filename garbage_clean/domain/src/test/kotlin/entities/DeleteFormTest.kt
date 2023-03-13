package entities

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import yin_kio.garbage_clean.domain.entities.DeleteForm
import yin_kio.garbage_clean.domain.entities.FormItem
import yin_kio.garbage_clean.domain.entities.GarbageType

class DeleteFormTest {


    private lateinit var deleteForm: DeleteForm


    @BeforeEach
    fun setup(){
        deleteForm = DeleteForm()
    }


    @Test
    fun testUnique(){
        deleteForm.add(FormItem(GarbageType.Apk, 0))
        deleteForm.add(FormItem(GarbageType.Apk, 0))

        assertEquals(1, deleteForm.size)
    }

    @Test
    fun testCanFree(){
        deleteForm.add(FormItem(GarbageType.Apk, 5))
        deleteForm.add(FormItem(GarbageType.Temp, 5))

        assertEquals(10, deleteForm.canFree)
    }

    @Test
    fun testSwitchSelection(){

        deleteForm.apply {
            add(FormItem(GarbageType.Temp, 10))
            add(FormItem(GarbageType.Apk, 10))
            assertFalse(isAllSelected)

            switchSelection(GarbageType.Apk)
            assertTrue(deleteRequest.contains(GarbageType.Apk))
            assertFalse(isAllSelected)

            switchSelection(GarbageType.Apk)
            assertTrue(deleteRequest.isEmpty())
            assertFalse(isAllSelected)

            switchSelection(GarbageType.Apk)
            switchSelection(GarbageType.Temp)
            assertTrue(isAllSelected)

            switchSelection(GarbageType.Temp)
            assertFalse(isAllSelected)
        }


    }

    @Test
    fun testSwitchSelectAll(){
        deleteForm.addAll(GarbageType.values().map { FormItem(it, 0) })
        deleteForm.switchSelectAll()
        assertTrue( deleteForm.deleteRequest.containsAll(GarbageType.values().toList()))
        assertTrue(deleteForm.isAllSelected)

        deleteForm.switchSelectAll()
        assertTrue(deleteForm.deleteRequest.isEmpty())
        assertFalse(deleteForm.isAllSelected)
    }

}