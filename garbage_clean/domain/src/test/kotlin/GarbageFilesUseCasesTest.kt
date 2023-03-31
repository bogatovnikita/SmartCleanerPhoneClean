import io.mockk.*
import org.junit.jupiter.api.Test
import yin_kio.garbage_clean.domain.entities.GarbageSelector
import yin_kio.garbage_clean.domain.gateways.Permissions
import yin_kio.garbage_clean.domain.services.GarbageFormsCreator
import yin_kio.garbage_clean.domain.services.garbage_files.GarbageType
import yin_kio.garbage_clean.domain.services.selectable_form.SelectableForm
import yin_kio.garbage_clean.domain.ui_out.Checkable
import yin_kio.garbage_clean.domain.ui_out.Garbage
import yin_kio.garbage_clean.domain.ui_out.GarbageOutCreator
import yin_kio.garbage_clean.domain.ui_out.UiOuter
import yin_kio.garbage_clean.domain.use_cases.GarbageFilesUseCases
import java.io.File

class GarbageFilesUseCasesTest {

    private val uiOuter: UiOuter = spyk()
    private val garbageSelector: GarbageSelector = spyk()
    private val permissions: Permissions = mockk()
    private val garbageFormsProvider: GarbageFormsCreator = mockk()
    private val garbageOutCreator: GarbageOutCreator = mockk()


    private val useCases = GarbageFilesUseCases(
        uiOuter = uiOuter,
        garbageSelector = garbageSelector,
        permissions = permissions,
        garbageFormsCreator = garbageFormsProvider,
        garbageOutCreator = garbageOutCreator
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
        val group = GarbageType.Apk
        val item = File("")
        coEvery { garbageSelector.isItemSelected(group, item) } returns isSelected

        useCases.switchItemSelection(group, item, itemCheckable)

        coVerifyOrder {
            garbageSelector.switchFileSelection(group, item)
            itemCheckable.setChecked(isSelected)
            uiOuter.updateGroup(group)
        }
    }

    @Test
    fun testSwitchGroupSelection(){
        assertPassGroupSelected(true)
        assertPassGroupSelected(false)
    }

    private fun assertPassGroupSelected(isGroupSelected: Boolean) {
        val group = GarbageType.Apk
        coEvery { garbageSelector.isGroupSelected(group) } returns isGroupSelected

        useCases.switchGroupSelection(group, groupCheckable)

        coVerifyOrder {
            garbageSelector.switchGroupSelected(group)
            groupCheckable.setChecked(isGroupSelected)
            uiOuter.updateGroup(group)
        }
    }

    @Test
    fun testScan(){
        assertScanIfHasPermission()
        assertScanIfHasNotPermission()
    }

    private fun assertScanIfHasPermission(){
        coEvery { permissions.hasPermission } returns true


        val garbageOut = listOf<Garbage>()
        val garbageForms = mapOf<GarbageType, SelectableForm<File>>()


        coEvery { garbageFormsProvider.provide() } returns garbageForms
        coEvery { garbageSelector.getGarbage() } returns garbageForms
        coEvery { garbageOutCreator.create(garbageForms) } returns garbageOut


        useCases.scan()

        coVerifyOrder {
            uiOuter.showUpdateProgress()
            garbageSelector.setGarbage(garbageForms)
            uiOuter.outGarbage(garbageOut)
        }
    }

    private fun assertScanIfHasNotPermission(){
        coEvery { permissions.hasPermission } returns false

        useCases.scan()

        coVerify { uiOuter.showPermissionDialog() }
    }

}