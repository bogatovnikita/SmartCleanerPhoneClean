package yin_kio.garbage_clean.presentation.garbage_list

import android.content.Context
import android.text.format.Formatter.formatFileSize
import com.bogatovnikita.language_dialog.language.Language
import com.smart.cleaner.phoneclean.ui_core.adapters.models.GeneralOptimizingItem
import yin_kio.garbage_clean.domain.services.garbage_files.GarbageType
import yin_kio.garbage_clean.domain.ui_out.Garbage
import yin_kio.garbage_clean.presentation.R
import yin_kio.garbage_clean.presentation.garbage_list.adapter.models.GarbageGroup
import java.io.File

class Presenter(
    context: Context
) {

    private val language = Language(context)
    private var context = language.changeLanguage()

    fun updateLanguage(){
        this.context = language.changeLanguage()
    }

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
            context.getString(R.string.junk_clean_button)
        } else {
            context.getString(R.string.junk_clean_scan)
        }
    }

    fun presentGarbageWithoutPermission() : List<GarbageGroup>{
        return getEmptyGarbageGroups()
    }



    fun presentGarbageForProgress() : List<GarbageGroup>{
        return getEmptyGarbageGroups(
            isInProgress = true,
            alpha = 1.0f
        )
    }


    fun presentGarbageName(garbageType: GarbageType) : String{

        val resId = when(garbageType){
            GarbageType.Apk -> R.string.junk_clean_apk
            GarbageType.Temp -> R.string.junk_clean_temp_files
            GarbageType.RestFiles -> R.string.junk_clean_residual_files
            GarbageType.EmptyFolders -> R.string.junk_clean_empty_filders
            GarbageType.Thumbnails -> R.string.junk_clean_miniatures
        }

        return context.getString(resId)

    }

    fun presentGarbage(garbage: List<Garbage>) : List<GarbageGroup>{
        if (garbage.isEmpty()){
            return getEmptyGarbageGroups()
        }

        return garbage.map {
            GarbageGroup(
                type = it.type,
                name = presentGarbageName(it.type),
                files = it.files,
                alpha = 1.0f,
                isEnabled = true,
                description = formatFileSize(context, it.files.sumOf { it.length() })
            )
        }
    }

    private fun getEmptyGarbageGroups(
        isInProgress: Boolean = false,
        alpha: Float = 0.5f,
    ) : List<GarbageGroup>{
        return GarbageType.values().map {
            GarbageGroup(
                type = it,
                name = presentGarbageName(it),
                isInProgress = isInProgress,
                alpha = alpha,
                description = formatFileSize(context, 0L)
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
            R.string.junk_clean_recomendation
        } else {
            R.string.junk_clean_amount_message
        }
        return context.getString(id)
    }

    fun presentMessage(garbage: List<Garbage>) : String{
        val id = if (garbage.isEmpty()){
            R.string.junk_clean_cleaning_result_message
        } else {
            R.string.junk_clean_recomendation
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

    fun presentNoPermissionSizeMessageColor() : Int {
        return context.getColor(general.R.color.error)
    }



    fun presentProgressSizeMessageColor(wasClean: Boolean) : Int{
        if (wasClean) return wasCleanColor()

        return context.getColor(general.R.color.error)
    }


    private fun wasCleanColor() = context.getColor(general.R.color.secondary)


}