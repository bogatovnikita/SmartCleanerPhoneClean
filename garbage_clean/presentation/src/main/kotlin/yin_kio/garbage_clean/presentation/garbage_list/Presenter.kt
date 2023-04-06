package yin_kio.garbage_clean.presentation.garbage_list

import android.content.Context
import android.text.format.Formatter.formatFileSize
import com.smart.cleaner.phoneclean.ui_core.adapters.models.GeneralOptimizingItem
import yin_kio.garbage_clean.domain.services.garbage_files.GarbageType
import yin_kio.garbage_clean.domain.ui_out.Garbage
import yin_kio.garbage_clean.presentation.R
import yin_kio.garbage_clean.presentation.garbage_list.adapter.models.GarbageGroup
import java.io.File

class Presenter(
    private val context: Context
) {

    fun presentSize(size: Long) : String{
        return formatFileSize(context, size)
    }

    fun presentUnknownSize() : String{
        return formatFileSize(context, 0L).replace("0", "(?)")
    }

    fun persentProgressSize() : String{
        return formatFileSize(context, 10_000_000).replace("10,00", "...")
            .replace("10.00", "...")
    }

    fun presentButtonText(hasPermission: Boolean) : String{
        return if (hasPermission){
            context.getString(R.string.clean)
        } else {
            context.getString(R.string.scan)
        }
    }

    fun presentGarbageWithoutPermission() : List<GarbageGroup>{
        return GarbageType.values().map {
            GarbageGroup(
                type = it,
                name = presentGarbageName(it),
                files = listOf(),
                isEnabled = false
            )
        }
    }

    fun presentGarbageForProgress() : List<GarbageGroup>{
        return GarbageType.values().map {
            GarbageGroup(
                type = it,
                name = presentGarbageName(it),
                files = listOf(),
                isInProgress = true,
                alpha = 1.0f,
                isEnabled = false
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

    fun presentGarbage(garbage: List<Garbage>) : List<GarbageGroup>{
        if (garbage.isEmpty()){
            return GarbageType.values().map {
                GarbageGroup(
                    type = it,
                    name = presentGarbageName(it),
                    files = emptyList(),
                    alpha = 0.5f,
                    isEnabled = false
                )
            }
        }

        return garbage.map {
            GarbageGroup(
                type = it.type,
                name = presentGarbageName(it.type),
                files = it.files,
                alpha = 1.0f,
                isEnabled = true
            )
        }
    }

    fun presentButtonOpacity(hasSelected: Boolean) : Float{
        return if (hasSelected) 1f else 0.5f
    }

    fun presentCleanProgressMessages(files: List<File>) : List<GeneralOptimizingItem>{
        return files.map { GeneralOptimizingItem(it.absolutePath) }
    }

    fun presentFreedSpace(space: Long) : String{
        return formatFileSize(context, space)
    }

    fun presentMessage(hasPermission: Boolean) : String{
        val id = if (hasPermission){
            R.string.message_clean_recomendation
        } else {
            R.string.garbage_amount_message
        }
        return context.getString(id)
    }

    fun presentMessage(garbage: List<Garbage>) : String{
        val id = if (garbage.isEmpty()){
            R.string.cleaning_result_message
        } else {
            R.string.message_clean_recomendation
        }
        return context.getString(id)
    }

    fun presentProgressMessageColor(garbage: List<Garbage>, wasClean: Boolean) : Int{
        if (wasClean) return wasCleanColor()

        val id = if (garbage.isEmpty()){
            general.R.color.secondary
        } else {
            general.R.color.error
        }
        return context.getColor(id)
    }


    fun presentProgressMessageColor(wasClean: Boolean) : Int{
        val id = if (wasClean){
            general.R.color.secondary
        } else {
            general.R.color.error
        }
        return context.getColor(id)
    }

    fun presentNoPermissionMessageColor() : Int{
        return context.getColor(general.R.color.primary)
    }

    fun presentSizeMessageColor(garbage: List<Garbage>, wasClean: Boolean) : Int{
        if (wasClean) return wasCleanColor()

        val id = if (garbage.isEmpty()){
            general.R.color.secondary
        } else {
            general.R.color.error
        }
        return context.getColor(id)
    }


    private fun wasCleanColor() = context.getColor(general.R.color.secondary)

    fun presentProgressSizeMessageColor(wasClean: Boolean) : Int{
        if (wasClean) return wasCleanColor()

        return context.getColor(general.R.color.error)
    }

}