import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.spyk
import org.junit.jupiter.api.Test
import yin_kio.garbage_clean.domain.entities.Selector
import yin_kio.garbage_clean.domain.ui_out.Checkable
import yin_kio.garbage_clean.domain.ui_out.UiOuter
import yin_kio.garbage_clean.domain.use_cases.GarbageFilesUseCases

class GarbageFilesUseCasesTest {

    private val uiOuter: UiOuter = spyk()

    private val selector: Selector = spyk()
    private val useCases = GarbageFilesUseCases(
        uiOuter = uiOuter,
        selector = selector
    )

    private val itemCheckable: Checkable = spyk()
    private val groupCheckable: Checkable = spyk()

    @Test
    fun testClosePermissionDialog(){
        useCases.closePermissionDialog()

        coVerify { uiOuter.closePermissionDialog() }
    }

    @Test
    fun testRequestPermission(){
        useCases.requestPermission()

        coVerify { uiOuter.requestPermission() }
    }

    @Test
    fun testSwitchItemSelection(){
        assertItemSelectionPassed(true)
        assertItemSelectionPassed(false)
    }

    private fun assertItemSelectionPassed(isSelected: Boolean) {
        val groupIndex = 0
        val itemIndex = 0
        coEvery { selector.isItemSelected(groupIndex, itemIndex) } returns isSelected

        useCases.switchItemSelection(groupIndex, itemIndex, itemCheckable)

        coVerify {
            selector.switchItemSelection(groupIndex, itemIndex)
            itemCheckable.setChecked(isSelected)
            uiOuter.updateGroup(groupIndex)
        }
    }

    @Test
    fun testSwitchGroupSelection(){
        assertPassGroupSelected(true)
        assertPassGroupSelected(false)
    }

    private fun assertPassGroupSelected(isGroupSelected: Boolean) {
        val groupIndex = 0
        coEvery { selector.isGroupSelected(groupIndex) } returns isGroupSelected

        useCases.switchGroupSelection(groupIndex, groupCheckable)

        coVerify {
            selector.switchGroupSelected(groupIndex)
            groupCheckable.setChecked(isGroupSelected)
            uiOuter.updateGroup(groupIndex)
        }
    }

}