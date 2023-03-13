import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import yin_kio.garbage_clean.domain.entities.DeleteRequest
import yin_kio.garbage_clean.domain.entities.GarbageFiles
import yin_kio.garbage_clean.domain.entities.GarbageType
import yin_kio.garbage_clean.domain.gateways.Ads
import yin_kio.garbage_clean.domain.gateways.Files
import yin_kio.garbage_clean.domain.gateways.NoDeletableFiles
import yin_kio.garbage_clean.domain.out.DeleteFormOut
import yin_kio.garbage_clean.domain.out.DeleteProgressState
import yin_kio.garbage_clean.domain.out.OutBoundary
import yin_kio.garbage_clean.domain.services.DeleteFormMapper
import yin_kio.garbage_clean.domain.use_cases.GarbageCleanerUseCasesImpl
import yin_kio.garbage_clean.domain.use_cases.UpdateUseCase


@OptIn(ExperimentalCoroutinesApi::class)
class GarbageCleanerUseCasesTest {

    private val files: Files = mockk()
    private val outBoundary: OutBoundary = spyk()
    private val mapper: DeleteFormMapper = mockk()
    private val updateUseCase: UpdateUseCase = mockk()
    private val ads: Ads = mockk()
    private val noDeletableFiles: NoDeletableFiles = spyk()
    private lateinit var useCases: GarbageCleanerUseCasesImpl
    private val garbageFiles: GarbageFiles = spyk()

    private val deleteFormOut = DeleteFormOut()


    init {
        coEvery { mapper.createDeleteFormOut(garbageFiles.deleteForm) } returns deleteFormOut
        coEvery {
            files.deleteAndGetNoDeletable(listOf(APK, TEMP))
            files.deleteAndGetNoDeletable(listOf())
        } returns listOf()
        coEvery {

            garbageFiles.deleteForm.switchSelectAll()
            garbageFiles.deleteForm.switchSelection(GarbageType.Apk)

            updateUseCase.update()

            ads.preloadAd()
        } returns Unit
    }

    private fun setupTest(testBody: suspend TestScope.() -> Unit){
        runTest {
            useCases = GarbageCleanerUseCasesImpl(
                files = files,
                garbageFiles = garbageFiles,
                coroutineScope = this,
                outBoundary = outBoundary,
                mapper = mapper,
                updateUseCase = updateUseCase,
                ads = ads,
                dispatcher = coroutineContext,
                noDeletableFiles = noDeletableFiles
            )
            testBody()
        }
    }


    @Test
    fun testSwitchSelectAll() = setupTest {
        useCases.switchSelectAll()
        wait()

        coVerify {
            garbageFiles.deleteForm.switchSelectAll()
            outBoundary.outDeleteForm(deleteFormOut)
        }
    }


    @Test
    fun testSwitchSelection() = setupTest{
        useCases.switchSelection(GarbageType.Apk)
        wait()

        coVerify { garbageFiles.deleteForm.switchSelection(GarbageType.Apk) }
        coVerify { outBoundary.outDeleteForm(DeleteFormOut()) }
    }

    @Test
    fun `test deleteIfCan deleteRequest is not empty`() = setupTest{
        coEvery { garbageFiles.deleteForm.deleteRequest } returns DeleteRequest().apply {
            add(GarbageType.Apk)
            add(GarbageType.Temp)

            garbageFiles[GarbageType.Apk] = mutableSetOf(APK)
            garbageFiles[GarbageType.Temp] = mutableSetOf(TEMP)
        }
        coEvery { files.deleteAndGetNoDeletable(listOf(APK, TEMP)) } returns listOf()

        useCases.deleteIfCan()
        wait()

        coVerifyOrder {
            ads.preloadAd()
            outBoundary.outDeleteProgress(DeleteProgressState.Progress)
            outBoundary.outDeleteRequest(listOf(GarbageType.Apk, GarbageType.Temp))
            files.deleteAndGetNoDeletable(listOf(APK, TEMP))
            noDeletableFiles.save(listOf())
            outBoundary.outDeletedSize(0)
            outBoundary.outDeleteProgress(DeleteProgressState.Complete)
        }
    }


    @Test
    fun `test deleteIfCan deleteRequest is empty`() = setupTest{
        coEvery { garbageFiles.deleteForm.deleteRequest } returns DeleteRequest()

        useCases.deleteIfCan()

        coVerify(inverse = true) { files.deleteAndGetNoDeletable(listOf()) }
    }

    @Test
    fun testUpdate() = setupTest{
        useCases.update()

        coVerify { updateUseCase.update() }
    }

    @Test
    fun testClose() = setupTest {
        useCases.close()

        coVerify { outBoundary.outIsClosed(true) }
    }



    private fun TestScope.wait() {
        advanceUntilIdle()
    }

    companion object{
        private const val APK = "apk"
        private const val TEMP = "temp"
    }

}