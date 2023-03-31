import io.mockk.coEvery
import io.mockk.coVerifyOrder
import io.mockk.mockk
import io.mockk.spyk
import org.junit.jupiter.api.Test
import yin_kio.garbage_clean.domain.entities.GarbageSelector
import yin_kio.garbage_clean.domain.services.GarbageFormsCreator
import yin_kio.garbage_clean.domain.services.garbage_files.GarbageType
import yin_kio.garbage_clean.domain.services.selectable_form.SelectableForm
import yin_kio.garbage_clean.domain.ui_out.Garbage
import yin_kio.garbage_clean.domain.ui_out.GarbageOutCreator
import yin_kio.garbage_clean.domain.ui_out.UiOuter
import yin_kio.garbage_clean.domain.use_cases.UpdateUseCase
import java.io.File

class UpdateUseCaseTest {

    private val uiOuter: UiOuter = spyk()
    private val garbageSelector: GarbageSelector = spyk()
    private val garbageFormsProvider: GarbageFormsCreator = mockk()
    private val garbageOutCreator: GarbageOutCreator = mockk()

    private val useCase = UpdateUseCase(
        uiOuter = uiOuter,
        garbageSelector = garbageSelector,
        garbageFormsProvider = garbageFormsProvider,
        garbageOutCreator = garbageOutCreator,
    )

    @Test
    fun testUpdate(){

        val garbageOut = listOf<Garbage>()
        val garbageForms = mapOf<GarbageType, SelectableForm<File>>()


        coEvery { garbageFormsProvider.provide() } returns garbageForms
        coEvery { garbageSelector.getGarbage() } returns garbageForms
        coEvery { garbageOutCreator.create(garbageForms) } returns garbageOut

        useCase.update()

        coVerifyOrder {
            uiOuter.showUpdateProgress()
            garbageSelector.setGarbage(garbageForms)
            uiOuter.outGarbage(garbageOut)
        }
    }

}