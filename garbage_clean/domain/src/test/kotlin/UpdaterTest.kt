import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import yin_kio.garbage_clean.domain.entities.FileSystemInfo
import yin_kio.garbage_clean.domain.garbage_files.GarbageFiles
import yin_kio.garbage_clean.domain.gateways.FileSystemInfoProvider
import yin_kio.garbage_clean.domain.gateways.Files
import yin_kio.garbage_clean.domain.gateways.NoDeletableFiles
import yin_kio.garbage_clean.domain.gateways.Permissions
import yin_kio.garbage_clean.domain.out.DeleteFormOut
import yin_kio.garbage_clean.domain.out.Navigator
import yin_kio.garbage_clean.domain.out.Outer
import yin_kio.garbage_clean.domain.services.DeleteFormMapper
import yin_kio.garbage_clean.domain.use_cases.Updater


@OptIn(ExperimentalCoroutinesApi::class)
class UpdaterTest {

    private val outer: Outer = spyk()
    private val fileSystemInfoProvider: FileSystemInfoProvider = mockk()
    private val permissions: Permissions = mockk()
    private val garbageFiles: GarbageFiles = spyk()
    private val files: Files = mockk()
    private val noDeletableFiles: NoDeletableFiles = spyk()
    private lateinit var updater: Updater

    private val fileSystemInfo = FileSystemInfo()
    private val deleteFormOut = DeleteFormOut()

    private val navigator: Navigator = spyk()


    init {
        coEvery { files.getAll() } returns listOf()
    }

    @Test
    fun `testUpdate with has permission`() = setupTest{
        coEvery { permissions.hasStoragePermission } returns true

        updater.update(navigator)
        wait()

        assertUpdateOrder(fileSystemInfo, deleteFormOut)
    }

    private fun assertUpdateOrder(
        fileSystemInfo: FileSystemInfo,
        deleteFormOut: DeleteFormOut
    ) {
        coVerifyOrder {
            outer.outUpdateProgress(true)
            outer.outFileSystemInfo(fileSystemInfo)
            garbageFiles.setFiles(listOf())
            outer.outDeleteForm(deleteFormOut)
            outer.outUpdateProgress(false)
        }
    }

    @Test
    fun `test update without permission`() = setupTest{
        coEvery { permissions.hasStoragePermission } returns false

        updater.update(navigator)
        wait()

        coVerify { navigator.showPermission() }
    }

















    private fun setupTest(testBody: suspend TestScope.() -> Unit){
        runTest {

            coEvery { fileSystemInfoProvider.getFileSystemInfo() } returns FileSystemInfo()

            updater = Updater(
                outer = outer,
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