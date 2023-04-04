package use_cases

import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import yin_kio.garbage_clean.domain.entities.GarbageSelector
import yin_kio.garbage_clean.domain.gateways.Permissions
import yin_kio.garbage_clean.domain.gateways.StorageInfo
import yin_kio.garbage_clean.domain.services.garbage_files.GarbageType
import yin_kio.garbage_clean.domain.ui_out.Checkable
import yin_kio.garbage_clean.domain.ui_out.UiOuter
import yin_kio.garbage_clean.domain.use_cases.*
import java.io.File


@OptIn(ExperimentalCoroutinesApi::class)
class GarbageFilesUseCasesImplTest {

    private val uiOuter: UiOuter = spyk()
    private val garbageSelector: GarbageSelector = spyk()
    private val permissions: Permissions = mockk()
    private val updateUseCase: UpdateUseCase = mockk{
        coEvery { update() } returns Unit
    }
    private val cleanUseCase: CleanUseCase = mockk{
        coEvery { clean() } returns Unit
    }
    private val storageInfo: StorageInfo = spyk()

    private val dispatcher = StandardTestDispatcher()
    private val testScope = TestScope(dispatcher)

    private val scanUseCase: ScanUseCase = mockk{
        coEvery { scan() } returns Unit
    }
    private val updateState: UpdateStateHolder = mockk()


    private val useCases = GarbageFilesUseCasesImpl(
        uiOuter = uiOuter,
        garbageSelector = garbageSelector,
        permissions = permissions,
        updateUseCase = updateUseCase,
        storageInfo = storageInfo,
        coroutineScope = testScope,
        dispatcher = dispatcher,
        cleanUseCase = cleanUseCase,
        scanUseCase = scanUseCase,
        updateState = updateState
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
        coEvery { permissions.hasPermission } returns true

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
        coEvery { permissions.hasPermission } returns true

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
            uiOuter.updateChildrenAndGroup(group)
        }
    }

    @Test
    fun testScanOrClean() = testScope.runTest {
        assertUnsuccessfullScan()
        assertSuccesfullScan()
    }

    private fun TestScope.assertUnsuccessfullScan() {
        coEvery { updateState.updateState } returns UpdateState.Progress

        useCases.scanOrClean()
        advanceUntilIdle()

        coVerify { scanUseCase.scan() }
    }

    private fun TestScope.assertSuccesfullScan() {
        coEvery { updateState.updateState } returns UpdateState.Successful

        useCases.scanOrClean()
        advanceUntilIdle()

        coVerify { cleanUseCase.clean() }
    }

    @Test
    fun testStart() = testScope.runTest{
        assertStartWithPermission()
        assertStartWithoutPermission()
    }

    private fun TestScope.assertStartWithPermission(){
        coEvery { permissions.hasPermission } returns true

        useCases.start()
        advanceUntilIdle()

        coVerify { updateUseCase.update() }
    }

    private fun TestScope.assertStartWithoutPermission(){
        coEvery { permissions.hasPermission } returns false

        useCases.start()
        advanceUntilIdle()

        coVerify { uiOuter.showPermissionRequired() }
    }


    @Test
    fun testCloseInter(){
        val result = 0L
        coEvery { storageInfo.freedVolume } returns result

        useCases.closeInter()

        coVerify { uiOuter.showResult(result) }
    }

}