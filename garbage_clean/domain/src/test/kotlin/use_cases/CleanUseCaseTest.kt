package use_cases

import io.mockk.coEvery
import io.mockk.coVerifyOrder
import io.mockk.spyk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import yin_kio.garbage_clean.domain.entities.GarbageSelector
import yin_kio.garbage_clean.domain.gateways.Files
import yin_kio.garbage_clean.domain.gateways.StorageInfo
import yin_kio.garbage_clean.domain.services.garbage_files.GarbageType
import yin_kio.garbage_clean.domain.ui_out.UiOuter
import yin_kio.garbage_clean.domain.use_cases.CleanUseCase
import java.io.File


@OptIn(ExperimentalCoroutinesApi::class)
class CleanUseCaseTest {

    private val uiOuter: UiOuter = spyk()
    private val garbageSelector: GarbageSelector = spyk()
    private val storageInfo: StorageInfo = spyk()
    private val files: Files = spyk()


    private val useCase = CleanUseCase(
        uiOuter = uiOuter,
        garbageSelector = garbageSelector,
        storageInfo = storageInfo,
        files = files,
    )

    @Test
    fun testClean() = runTest{
        val selectedFiles = listOf(File(""))
        // Добавить реализацию очистки

        coEvery { garbageSelector.getSelected() } returns selectedFiles
        coEvery { storageInfo.freedVolume } returns 0

        useCase.clean()

        coVerifyOrder {
            storageInfo.saveStartVolume()
            uiOuter.showCleanProgress(selectedFiles)
            files.deleteFiles(selectedFiles)
            storageInfo.saveEndVolume()
            storageInfo.calculateEndVolume()
            uiOuter.showResult(0)
        }
    }

}