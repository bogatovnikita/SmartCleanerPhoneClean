import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import yin_kio.garbage_clean.domain.selector.GroupSelectableForm
import yin_kio.garbage_clean.domain.selector.BaseSelectableForm

class GroupSelectableFormTest {

    private val groupForm = GroupSelectableForm<String>()
    private val firstForm = BaseSelectableForm<String>()
    private val secondForm = BaseSelectableForm<String>()

    @Test
    fun testIsAllSelected(){
        groupForm.content = listOf(firstForm, secondForm)

        firstForm.content = listOf("")
        secondForm.content = listOf("")

        groupForm.switchItemSelection(firstForm)
        assertTrue(groupForm.isItemSelected(firstForm))

        groupForm.switchItemSelection(secondForm)
        assertTrue(groupForm.isItemSelected(secondForm))

        assertFalse(groupForm.isAllSelected)

        firstForm.switchItemSelection("")
        secondForm.switchItemSelection("")
        assertTrue(groupForm.isAllSelected)


    }


}