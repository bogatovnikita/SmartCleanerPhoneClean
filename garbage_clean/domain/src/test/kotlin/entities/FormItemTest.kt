package entities

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Test
import yin_kio.garbage_clean.domain.entities.FormItem
import yin_kio.garbage_clean.domain.entities.GarbageType

class FormItemTest {

    @Test
    fun testEquals(){
        val apkItem1 = FormItem(GarbageType.Apk, 0)
        val apkItem2 = FormItem(GarbageType.Apk, 1)

        assertEquals(apkItem1, apkItem2)


        val tempItem = FormItem(GarbageType.Temp, 0)
        assertNotEquals(apkItem1, tempItem)
    }

}