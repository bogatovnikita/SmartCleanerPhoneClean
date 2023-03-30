import io.mockk.coVerify
import io.mockk.spyk
import org.junit.jupiter.api.Test
import yin_kio.garbage_clean.domain.ui_out.UiOuter
import yin_kio.garbage_clean.domain.use_cases.GarbageFilesUseCases

class GarbageFilesUseCasesTest {

    private val uiOuter: UiOuter = spyk()

    private val useCases = GarbageFilesUseCases(uiOuter)

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

}