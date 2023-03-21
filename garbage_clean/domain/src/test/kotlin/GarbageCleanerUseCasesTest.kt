import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import yin_kio.garbage_clean.domain.entities.DeleteRequest
import yin_kio.garbage_clean.domain.entities.GarbageFiles
import yin_kio.garbage_clean.domain.entities.GarbageType
import yin_kio.garbage_clean.domain.gateways.Files
import yin_kio.garbage_clean.domain.out.DeleteFormOut
import yin_kio.garbage_clean.domain.out.Navigator
import yin_kio.garbage_clean.domain.out.Outer
import yin_kio.garbage_clean.domain.services.DeleteFormMapper
import yin_kio.garbage_clean.domain.use_cases.DeleteUseCase
import yin_kio.garbage_clean.domain.use_cases.GarbageCleanerUseCasesImpl
import yin_kio.garbage_clean.domain.use_cases.Updater


@OptIn(ExperimentalCoroutinesApi::class)
class GarbageCleanerUseCasesTest {

    private val files: Files = mockk()
    private val outer: Outer = spyk()
    private val mapper: DeleteFormMapper = mockk()
    private val updater: Updater = mockk()
    private val garbageFiles: GarbageFiles = spyk()
    private val deleteFormOut = DeleteFormOut()
    private val navigator: Navigator = spyk()
    private val deleteUseCase: DeleteUseCase = mockk()

    private val dispatcher = StandardTestDispatcher()
    private val testScope = TestScope(dispatcher)
    private val useCases: GarbageCleanerUseCasesImpl = GarbageCleanerUseCasesImpl(
        garbageFiles = garbageFiles,
        coroutineScope = testScope,
        outer = outer,
        mapper = mapper,
        updater = updater,
        dispatcher = dispatcher,
        deleteUseCase = deleteUseCase,
    )


    init {
        coEvery { mapper.createDeleteFormOut(garbageFiles.deleteForm) } returns deleteFormOut
        coEvery {
            files.deleteAndGetNoDeletable(listOf(APK, TEMP))
            files.deleteAndGetNoDeletable(listOf())
        } returns listOf()
        coEvery {

            garbageFiles.deleteForm.switchSelectAll()
            garbageFiles.deleteForm.switchSelection(GarbageType.Apk)

            updater.update(navigator)

            deleteUseCase.delete(navigator)

        } returns Unit
    }




    @Test
    fun testSwitchSelectAll() = runTest {
        useCases.switchSelectAll()
        wait()

        coVerify {
            garbageFiles.deleteForm.switchSelectAll()
            outer.outDeleteForm(deleteFormOut)
        }
    }


    @Test
    fun testSwitchSelection() = runTest {
        useCases.switchSelection(GarbageType.Apk)
        wait()

        coVerify { garbageFiles.deleteForm.switchSelection(GarbageType.Apk) }
        coVerify { outer.outDeleteForm(DeleteFormOut()) }
    }

    @Test
    fun `test deleteIfCan deleteRequest is not empty`() = runTest {
        useCases.deleteIfCan(navigator)
        wait()


        coVerify { deleteUseCase.delete(navigator) }
    }


    @Test
    fun `test deleteIfCan deleteRequest is empty`() = runTest {
        coEvery { garbageFiles.deleteForm.deleteRequest } returns DeleteRequest()

        useCases.deleteIfCan(navigator)

        coVerify(inverse = true) { files.deleteAndGetNoDeletable(listOf()) }
    }

    @Test
    fun testUpdate() = runTest {
        useCases.update(navigator)

        coVerify { updater.update(navigator) }
    }

    @Test
    fun testClose() = runTest {
        useCases.close(navigator)

        coVerify { navigator.close() }
    }



    private fun runTest(testBody: suspend TestScope.() -> Unit){
        testScope.runTest {
            testBody()
        }
    }

    companion object{
        private const val APK = "apk"
        private const val TEMP = "temp"
    }

}