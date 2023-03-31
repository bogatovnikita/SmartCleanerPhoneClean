import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import yin_kio.garbage_clean.domain.entities.selectable_form.SimpleSelectableForm

class SelectableFormTest {

    private val form = SimpleSelectableForm<String>()

    @Test
    fun testSwitchItemSelection(){
        form.content = listOf("")
        assertTrue(form.content.isNotEmpty())

        form.switchItemSelection("")
        assertTrue(form.isItemSelected(""))

        form.switchItemSelection("")
        assertFalse(form.isItemSelected(""))
    }


    @Test
    fun testSwitchAllSelection(){
        form.content = listOf("")

        form.switchAllSelection()
        assertTrue(form.isAllSelected)

        form.switchAllSelection()
        assertFalse(form.isAllSelected)
    }

    @Test
    fun testSetContent(){
        form.content = listOf("")
        form.switchItemSelection("")
        assertFalse(form.selected.isEmpty())

        form.content = listOf("")
        assertTrue(form.selected.isEmpty())
    }
}