package use_cases

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.spyk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import yin_kio.garbage_clean.domain.gateways.Permissions
import yin_kio.garbage_clean.domain.ui_out.UiOuter
import yin_kio.garbage_clean.domain.use_cases.UpdateStateHolder
import yin_kio.garbage_clean.domain.use_cases.ScanUseCase
import yin_kio.garbage_clean.domain.use_cases.UpdateUseCase


@OptIn(ExperimentalCoroutinesApi::class)
class ScanUseCaseTest {

    private val permissions: Permissions = mockk()
    private val uiOuter: UiOuter = spyk()
    private val updateUseCase: UpdateUseCase = mockk{
        coEvery { update() } returns Unit
    }
    private val updateState: UpdateStateHolder = spyk()


    private val useCase = ScanUseCase(
        permissions = permissions,
        uiOuter = uiOuter,
        updateUseCase = updateUseCase,
    )

    @Test
    fun testScan() = runTest{
        assertScanIfHasNotPermission()
        assertScanIfHasPermission()
    }


    private suspend fun assertScanIfHasPermission() {
        coEvery { permissions.hasPermission } returns true

        useCase.scan()

        coVerify {
            updateUseCase.update()
        }
    }

    private suspend fun assertScanIfHasNotPermission() {
        coEvery { permissions.hasPermission } returns false

        useCase.scan()

        coVerify {
            uiOuter.showPermissionDialog()
        }
    }
}