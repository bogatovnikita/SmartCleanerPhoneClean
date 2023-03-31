import io.mockk.*
import org.junit.jupiter.api.Test
import yin_kio.garbage_clean.domain.entities.GarbageSelector
import yin_kio.garbage_clean.domain.gateways.Permissions
import yin_kio.garbage_clean.domain.ui_out.Checkable
import yin_kio.garbage_clean.domain.ui_out.Garbage
import yin_kio.garbage_clean.domain.ui_out.UiOuter
import yin_kio.garbage_clean.domain.use_cases.GarbageFilesUseCases

class GarbageFilesUseCasesTest {

    private val uiOuter: UiOuter = spyk()
    private val garbageSelector: GarbageSelector = spyk()
    private val permissions: Permissions = mockk()

    private val useCases = GarbageFilesUseCases(
        uiOuter = uiOuter,
        garbageSelector = garbageSelector,
        permissions = permissions
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
        coEvery { garbageSelector.isItemSelected(groupIndex, itemIndex) } returns isSelected

        useCases.switchItemSelection(groupIndex, itemIndex, itemCheckable)

        coVerifyOrder {
            garbageSelector.switchItemSelection(groupIndex, itemIndex)
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
        coEvery { garbageSelector.isGroupSelected(groupIndex) } returns isGroupSelected

        useCases.switchGroupSelection(groupIndex, groupCheckable)

        coVerifyOrder {
            garbageSelector.switchGroupSelected(groupIndex)
            groupCheckable.setChecked(isGroupSelected)
            uiOuter.updateGroup(groupIndex)
        }
    }

    @Test
    fun testScan(){
        assertScanIfHasPermission()
        assertScanIfHasNotPermission()
    }

    private fun assertScanIfHasPermission(){
        val garbage = listOf<Garbage>()
        coEvery { permissions.hasPermission } returns true
        coEvery { garbageSelector.getGarbage() } returns garbage

        useCases.scan()

        coVerify {
            uiOuter.outGarbage(garbage)
        }
    }

    private fun assertScanIfHasNotPermission(){
        coEvery { permissions.hasPermission } returns false

        useCases.scan()

        coVerify { uiOuter.showPermissionDialog() }
    }

}