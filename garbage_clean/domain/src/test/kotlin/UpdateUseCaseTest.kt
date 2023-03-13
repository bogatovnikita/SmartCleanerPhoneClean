import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import yin_kio.garbage_clean.domain.use_cases.UpdateUseCase
import yin_kio.garbage_clean.domain.entities.FileSystemInfo
import yin_kio.garbage_clean.domain.entities.GarbageFiles
import yin_kio.garbage_clean.domain.gateways.FileSystemInfoProvider
import yin_kio.garbage_clean.domain.gateways.Files
import yin_kio.garbage_clean.domain.gateways.NoDeletableFiles
import yin_kio.garbage_clean.domain.gateways.Permissions
import yin_kio.garbage_clean.domain.services.DeleteFormMapper
import yin_kio.garbage_clean.domain.out.DeleteFormOut
import yin_kio.garbage_clean.domain.out.DeleteProgressState
import yin_kio.garbage_clean.domain.out.OutBoundary


@OptIn(ExperimentalCoroutinesApi::class)
class UpdateUseCaseTest {

    private val outBoundary: OutBoundary = spyk()
    private val fileSystemInfoProvider: FileSystemInfoProvider = mockk()
    private val permissions: Permissions = mockk()
    private val garbageFiles: GarbageFiles = spyk()
    private val files: Files = mockk()
    private val noDeletableFiles: NoDeletableFiles = spyk()
    private lateinit var updateUseCase: UpdateUseCase

    private val fileSystemInfo = FileSystemInfo()
    private val deleteFormOut = DeleteFormOut()


    init {
        coEvery { files.getAll() } returns listOf()
    }

    @Test
    fun `testUpdate with has permission`() = setupTest{
        coEvery { permissions.hasStoragePermission } returns true

        updateUseCase.update()
        wait()

        assertUpdateOrder(fileSystemInfo, deleteFormOut)
    }

    private fun assertUpdateOrder(
        fileSystemInfo: FileSystemInfo,
        deleteFormOut: DeleteFormOut
    ) {
        coVerifyOrder {
            outBoundary.outDeleteProgress(DeleteProgressState.Wait)
            outBoundary.outHasPermission(true)
            outBoundary.outUpdateProgress(true)
            outBoundary.outFileSystemInfo(fileSystemInfo)
            garbageFiles.setFiles(listOf())
            outBoundary.outDeleteForm(deleteFormOut)
            outBoundary.outUpdateProgress(false)
        }
    }

    @Test
    fun `test update without permission`() = setupTest{
        coEvery { permissions.hasStoragePermission } returns false

        updateUseCase.update()
        wait()

        coVerify { outBoundary.outHasPermission(false) }
    }

















    private fun setupTest(testBody: suspend TestScope.() -> Unit){
        runTest {

            coEvery { fileSystemInfoProvider.getFileSystemInfo() } returns FileSystemInfo()

            updateUseCase = UpdateUseCase(
                outBoundary = outBoundary,
                coroutineScope = this,
                mapper = DeleteFormMapper(),
                garbageFiles = garbageFiles,
                fileSystemInfoProvider = fileSystemInfoProvider,
                permissions = permissions,
                files = files,
                dispatcher = coroutineContext,
                noDeletableFiles = noDeletableFiles
            )
            testBody()
        }
    }


    private fun TestScope.wait() {
        advanceUntilIdle()
    }

}