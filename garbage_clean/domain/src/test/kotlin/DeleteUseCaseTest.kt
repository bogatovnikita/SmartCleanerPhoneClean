import io.mockk.coEvery
import io.mockk.coVerifyOrder
import io.mockk.spyk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import yin_kio.garbage_clean.domain.entities.DeleteRequest
import yin_kio.garbage_clean.domain.entities.GarbageFiles
import yin_kio.garbage_clean.domain.entities.GarbageFiles.Companion.APK
import yin_kio.garbage_clean.domain.entities.GarbageFiles.Companion.TEMP
import yin_kio.garbage_clean.domain.entities.GarbageType
import yin_kio.garbage_clean.domain.gateways.Ads
import yin_kio.garbage_clean.domain.gateways.Files
import yin_kio.garbage_clean.domain.gateways.NoDeletableFiles
import yin_kio.garbage_clean.domain.out.Navigator
import yin_kio.garbage_clean.domain.out.Outer
import yin_kio.garbage_clean.domain.use_cases.DeleteUseCase

@OptIn(ExperimentalCoroutinesApi::class)
class DeleteUseCaseTest {

    private val garbageFiles: GarbageFiles = spyk()
    private val files: Files = spyk()
    private val outer: Outer = spyk()
    private val ads: Ads = spyk()
    private val noDeletableFiles: NoDeletableFiles = spyk()

    private lateinit var useCase: DeleteUseCase

    private val navigator: Navigator = spyk()


    @Test
    fun testDelete() = setupTest{
        coEvery { garbageFiles.deleteForm.deleteRequest } returns DeleteRequest().apply {
            add(GarbageType.Apk)
            add(GarbageType.Temp)

            garbageFiles[GarbageType.Apk] = mutableSetOf(APK)
            garbageFiles[GarbageType.Temp] = mutableSetOf(TEMP)
        }
        coEvery { files.deleteAndGetNoDeletable(listOf(APK, TEMP)) } returns listOf()

        useCase.delete(navigator)
        wait()

        coVerifyOrder {
            ads.preloadAd()
            navigator.showDeleteProgress()
            outer.outDeleteRequest(listOf(GarbageType.Apk, GarbageType.Temp))
            files.deleteAndGetNoDeletable(listOf(APK, TEMP))
            noDeletableFiles.save(listOf())
            outer.outDeletedSize(0)
            navigator.complete()
        }
    }

    private fun setupTest(testBody: suspend TestScope.() -> Unit){
        runTest {
            useCase = DeleteUseCase(
                garbageFiles = garbageFiles,
                ads = ads,
                coroutineScope = this,
                dispatcher = coroutineContext,
                files = files,
                noDeletableFiles = noDeletableFiles,
                outer = outer,

            )
            testBody()
        }
    }

    private fun TestScope.wait() {
        advanceUntilIdle()
    }

}