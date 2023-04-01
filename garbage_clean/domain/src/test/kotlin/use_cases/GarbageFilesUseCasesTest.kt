package use_cases

import io.mockk.*
import org.junit.jupiter.api.Test
import yin_kio.garbage_clean.domain.entities.GarbageSelector
import yin_kio.garbage_clean.domain.gateways.Permissions
import yin_kio.garbage_clean.domain.gateways.StorageInfo
import yin_kio.garbage_clean.domain.services.garbage_files.GarbageType
import yin_kio.garbage_clean.domain.ui_out.Checkable
import yin_kio.garbage_clean.domain.ui_out.UiOuter
import yin_kio.garbage_clean.domain.use_cases.GarbageFilesUseCases
import yin_kio.garbage_clean.domain.use_cases.UpdateUseCase
import java.io.File

class GarbageFilesUseCasesTest {

    private val uiOuter: UiOuter = spyk()
    private val garbageSelector: GarbageSelector = spyk()
    private val permissions: Permissions = mockk()
    private val updateUseCase: UpdateUseCase = mockk{
        coEvery { update() } returns Unit
    }
    private val storageInfo: StorageInfo = spyk()



    private val useCases = GarbageFilesUseCases(
        uiOuter = uiOuter,
        garbageSelector = garbageSelector,
        permissions = permissions,
        updateUseCase = updateUseCase,
        storageInfo = storageInfo
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

        useCases.scan()

        coVerify { updateUseCase.update() }
    }

    private fun assertScanIfHasNotPermission(){
        coEvery { permissions.hasPermission } returns false

        useCases.scan()

        coVerify { uiOuter.showPermissionDialog() }
    }

    @Test
    fun testStart(){
        assertStartWithPermission()
        assertStartWithoutPermission()
    }

    private fun assertStartWithPermission(){
        coEvery { permissions.hasPermission } returns true

        useCases.start()

        coVerify { updateUseCase.update() }
    }

    private fun assertStartWithoutPermission(){
        coEvery { permissions.hasPermission } returns false

        useCases.start()

        coVerify { uiOuter.showPermissionRequired() }
    }

    @Test
    fun testClean(){
        val messages = listOf<String>() // Уточнить, какие сообщения передавать на прогресс
        // Добавить реализацию очистки

        useCases.clean()

        coVerifyOrder {
            storageInfo.saveStartVolume()
            uiOuter.showCleanProgress(messages)
            storageInfo.saveEndVolume()
            storageInfo.calculateEndVolume()
        }
    }

    @Test
    fun testCloseInter(){
        val result = 0L
        coEvery { storageInfo.freedVolume } returns result

        useCases.closeInter()

        coVerify { uiOuter.showResult(result) }
    }

}