package use_cases

import io.mockk.coEvery
import io.mockk.coVerifyOrder
import io.mockk.mockk
import io.mockk.spyk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import yin_kio.garbage_clean.domain.entities.GarbageSelector
import yin_kio.garbage_clean.domain.services.CleanTracker
import yin_kio.garbage_clean.domain.services.garbage_forms_provider.GarbageFormsProvider
import yin_kio.garbage_clean.domain.services.garbage_files.GarbageType
import yin_kio.garbage_clean.domain.services.selectable_form.SelectableForm
import yin_kio.garbage_clean.domain.ui_out.Garbage
import yin_kio.garbage_clean.domain.ui_out.garbage_out_creator.GarbageOutCreator
import yin_kio.garbage_clean.domain.ui_out.UiOuter
import yin_kio.garbage_clean.domain.use_cases.UpdateState
import yin_kio.garbage_clean.domain.use_cases.UpdateStateHolder
import yin_kio.garbage_clean.domain.use_cases.UpdateUseCase
import java.io.File


@OptIn(ExperimentalCoroutinesApi::class)
class UpdateUseCaseTest {

    private val uiOuter: UiOuter = spyk()
    private val garbageSelector: GarbageSelector = spyk()
    private val garbageFormsProvider: GarbageFormsProvider = mockk()
    private val garbageOutCreator: GarbageOutCreator = mockk()
    private val updateStateHolder: UpdateStateHolder = spyk()
    private val cleanTracker: CleanTracker = mockk()

    private val useCase = UpdateUseCase(
        uiOuter = uiOuter,
        garbageSelector = garbageSelector,
        garbageFormsProvider = garbageFormsProvider,
        garbageOutCreator = garbageOutCreator,
        updateState = updateStateHolder,
        cleanTracker = cleanTracker
    )

    @Test
    fun testUpdate() = runTest{

        val garbageOut = listOf<Garbage>()
        val garbageForms = mapOf<GarbageType, SelectableForm<File>>()
        val wasClean = false

        coEvery { garbageFormsProvider.provide() } returns garbageForms
        coEvery { garbageSelector.getGarbage() } returns garbageForms
        coEvery { garbageOutCreator.create(garbageForms) } returns garbageOut
        coEvery { cleanTracker.wasClean } returns wasClean

        useCase.update()

        coVerifyOrder {
            uiOuter.showUpdateProgress(wasClean)
            garbageSelector.setGarbage(garbageForms)
            uiOuter.outGarbage(garbageOut, wasClean)
            updateStateHolder.updateState = UpdateState.Successful
        }
    }

}