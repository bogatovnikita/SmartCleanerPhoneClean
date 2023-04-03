package yin_kio.garbage_clean.presentation

import android.content.Context
import android.text.format.Formatter.formatFileSize
import yin_kio.garbage_clean.domain.services.garbage_files.GarbageType
import yin_kio.garbage_clean.presentation.adapter.models.GarbageGroup

class Presenter(
    private val context: Context
) {

    fun presentSize(size: Long) : String{
        return formatFileSize(context, size)
    }

    fun presentUnknownSize() : String{
        return formatFileSize(context, 0L).replace("0", "(?)")
    }

    fun presentButtonText(hasPermission: Boolean) : String{
        return context.getString(R.string.scan)
    }

    fun presentGarbageWithoutPermission() : List<GarbageGroup>{
        return GarbageType.values().map {
            GarbageGroup(
                type = it,
                name = presentGarbageName(it),
                files = listOf(),
            )
        }
    }

    private fun presentGarbageName(garbageType: GarbageType) : String{

        val resId = when(garbageType){
            GarbageType.Apk -> R.string.apk
            GarbageType.Temp -> R.string.temp_files
            GarbageType.RestFiles -> R.string.residual_files
            GarbageType.EmptyFolders -> R.string.empty_filders
            GarbageType.Thumbnails -> R.string.miniatures
        }

        return context.getString(resId)

    }

}