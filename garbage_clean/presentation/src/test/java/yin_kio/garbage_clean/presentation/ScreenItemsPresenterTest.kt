package yin_kio.garbage_clean.presentation

import android.content.Context
import android.text.format.Formatter.formatFileSize
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import yin_kio.garbage_clean.domain.entities.GarbageType
import yin_kio.garbage_clean.presentation.presenter.ScreenItemsPresenter

@RunWith(RobolectricTestRunner::class)
class ScreenItemsPresenterTest {

    private val context: Context = RuntimeEnvironment.getApplication()
    private val presenter = ScreenItemsPresenter(context)

    @Test
    fun `test presentIcon`(){
        assertEquals(R.drawable.ic_apk, presenter.presentIcon(GarbageType.Apk))
        assertEquals(R.drawable.ic_temp, presenter.presentIcon(GarbageType.Temp))
        assertEquals(R.drawable.ic_rest, presenter.presentIcon(GarbageType.RestFiles))
        assertEquals(R.drawable.ic_empty_folders, presenter.presentIcon(GarbageType.EmptyFolders))
        assertEquals(R.drawable.ic_thumb, presenter.presentIcon(GarbageType.Thumbnails))
    }

    @Test
    fun `test presentName`(){
        assertEquals(context.getString(R.string.apk_files), presenter.presentName(GarbageType.Apk))
        assertEquals(context.getString(R.string.temp_files), presenter.presentName(GarbageType.Temp))
        assertEquals(context.getString(R.string.rest_files), presenter.presentName(GarbageType.RestFiles))
        assertEquals(context.getString(R.string.miniatures), presenter.presentName(GarbageType.Thumbnails))
        assertEquals(context.getString(R.string.empty_folders), presenter.presentName(GarbageType.EmptyFolders))
    }

    @Test
    fun `test presentButtonName`(){
        assertEquals(context.getString(R.string.go_to_main_screen), presenter.presentButtonName(true))
        assertEquals(context.getString(R.string.delete), presenter.presentButtonName(false))
    }

    @Test
    fun `test presentButtonBg`(){
        assertEquals(general.R.drawable.bg_main_button_enabled, presenter.presentButtonBg(true))
        assertEquals(general.R.drawable.bg_main_button_disabled, presenter.presentButtonBg(false))
    }

    @Test
    fun `test presentCanFree`(){
        assertEquals(context.getString(R.string.can_free, formatFileSize(context, 0)), presenter.presentCanFree(0))
    }

    @Test
    fun `test presentFreed`(){
        assertEquals(context.getString(R.string.freed, formatFileSize(context, 0)), presenter.presentFreed(0))
    }

}