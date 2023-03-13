import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import yin_kio.garbage_clean.domain.entities.DeleteForm
import yin_kio.garbage_clean.domain.entities.FormItem
import yin_kio.garbage_clean.domain.entities.GarbageType
import yin_kio.garbage_clean.domain.services.DeleteFormMapper
import yin_kio.garbage_clean.domain.out.DeleteFormOut

internal class DeleteFormMapperTest{

    private val mapper = DeleteFormMapper()
    private val deleteForm = DeleteForm()

    @Test
    fun createDeleteFormOut(){
        deleteForm.addAll(GarbageType.values().map { FormItem(it, 0) })

        val noSelected = mapper.createDeleteFormOut(deleteForm)
        assertItemsMapping(noSelected)
        assertFalse(noSelected.canDelete)

        deleteForm.switchSelectAll()
        val allSelected = mapper.createDeleteFormOut(deleteForm)
        assertItemsMapping(allSelected)
        assertTrue(allSelected.canDelete)

        deleteForm.switchSelection(GarbageType.Temp)
        val containsSelected = mapper.createDeleteFormOut(deleteForm)
        assertEquals(false, containsSelected.isAllSelected)
        assertTrue(containsSelected.canDelete)
    }

    private fun assertItemsMapping(out: DeleteFormOut) {
        assertEquals(out.isAllSelected, deleteForm.isAllSelected)
        assertEquals(out.items.map { FormItem(it.garbageType, it.size) }, deleteForm.map { it })
    }

}