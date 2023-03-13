package yin_kio.garbage_clean.presentation.presenter

import android.content.Context
import android.text.format.Formatter.formatFileSize
import yin_kio.garbage_clean.domain.entities.GarbageType
import yin_kio.garbage_clean.presentation.R

class ScreenItemsPresenter(
    private val context: Context
) {

    fun presentIcon(garbageType: GarbageType) : Int{
        return when(garbageType){
            GarbageType.Apk -> R.drawable.ic_apk
            GarbageType.Temp -> R.drawable.ic_temp
            GarbageType.RestFiles -> R.drawable.ic_rest
            GarbageType.EmptyFolders -> R.drawable.ic_empty_folders
            GarbageType.Thumbnails -> R.drawable.ic_thumb
        }
    }

    fun presentName(garbageType: GarbageType) : String{
        return when(garbageType){
            GarbageType.Apk -> context.getString(R.string.apk_files)
            GarbageType.Temp -> context.getString(R.string.temp_files)
            GarbageType.RestFiles -> context.getString(R.string.rest_files)
            GarbageType.EmptyFolders -> context.getString(R.string.empty_folders)
            GarbageType.Thumbnails -> context.getString(R.string.miniatures)
        }
    }

    fun presentButtonName(deleteFormIsEmpty: Boolean) : String{
        return if (deleteFormIsEmpty){
            context.getString(R.string.go_to_main_screen)
        } else {
            context.getString(R.string.delete)
        }
    }

    fun presentButtonBg(hasSelected: Boolean) : Int{
        return if (hasSelected){
            general.R.drawable.bg_main_button_enabled
        } else {
            general.R.drawable.bg_main_button_disabled
        }
    }

    fun presentCanFree(size: Long) : String{
        return context.getString(R.string.can_free, formatFileSize(context, size))
    }

    fun presentFreed(size: Long) : String{
        return context.getString(R.string.freed, formatFileSize(context, size))
    }


}