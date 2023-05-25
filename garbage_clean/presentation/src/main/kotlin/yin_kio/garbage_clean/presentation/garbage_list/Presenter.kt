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


    fun presentButtonOpacity(hasSelected: Boolean) : Float{
        return if (hasSelected) 1f else 0.5f
    }

    fun presentCleanProgressMessages(files: List<File>) : List<GeneralOptimizingItem>{
        return files.map { GeneralOptimizingItem(it.absolutePath) }
    }

    fun presentFreedSpace(space: Long) : String{
        return formatFileSize(context, space)
    }

    fun presentNoPermissionMessageColor() : Int{
        return context.getColor(general.R.color.primary)
    }


    fun presentNoPermissionSizeMessageColor() : Int {
        return context.getColor(general.R.color.error)
    }







    fun presentStartWithoutPermissionScreen() : ScreenState{

        return ScreenState(
            sizeText = formatFileSize(context, 0L).replace("0", "(?)"),
            buttonText = context.getString(R.string.junk_clean_scan),
            garbageGroups = GarbageType.values().map {
                GarbageGroup(
                    type = it,
                    name = presentGarbageName(it),
                    description = ""
                )
            },
            isShowPermissionRequired = true,
            buttonOpacity = 1f,
            message = context.getString(R.string.junk_clean_amount_message),
            sizeMessageColor = context.getColor(general.R.color.error),
            messageColor = context.getColor(general.R.color.primary),
            isExpandEnabled = false,
            isInfoVisible = true,
        )
    }

    private fun presentGarbageName(garbageType: GarbageType) : String{

        val resId = when(garbageType){
            GarbageType.Apk -> R.string.junk_clean_apk
            GarbageType.Temp -> R.string.junk_clean_temp_files
            GarbageType.RestFiles -> R.string.junk_clean_residual_files
            GarbageType.EmptyFolders -> R.string.junk_clean_empty_filders
            GarbageType.Thumbnails -> R.string.junk_clean_miniatures
        }

        return context.getString(resId)

    }

    fun presentUpdatedScreen(oldState: ScreenState, garbage: List<Garbage>, isCleaned: Boolean) : ScreenState{

        return oldState.copy(
            sizeText = formatFileSize(context, garbage.sumOf { it.files.sumOf { it.length() } }),
            buttonText = context.getString(R.string.junk_clean_button),
            garbageGroups = presentGarbage(garbage),
            isShowPermissionRequired = false,
            buttonOpacity = 0.5f,
            message = presentMessage(garbage),
            messageColor = presentProgressMessageColor(garbage, isCleaned),
            sizeMessageColor = presentProgressMessageColor(garbage, isCleaned),
            isExpandEnabled = garbage.isNotEmpty(),
            isInfoVisible = true,
        )

    }


    private fun presentProgressMessageColor(garbage: List<Garbage>, isCleaned: Boolean) : Int{
        if (garbage.isEmpty()) return context.getColor(general.R.color.secondary)
        return presentProgressMessageColor(isCleaned)
    }


    private fun presentMessage(garbage: List<Garbage>) : String{
        val id = if (garbage.isEmpty()){
            R.string.junk_clean_cleaning_result_message
        } else {
            R.string.junk_clean_recomendation
        }
        return context.getString(id)
    }


    private fun presentGarbage(garbage: List<Garbage>) : List<GarbageGroup>{
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

    fun presentUpdateProgressScreen(oldState: ScreenState, isCleaned: Boolean) : ScreenState{

        return oldState.copy(
            sizeText = presentProgressSize(),
            buttonText = context.getString(R.string.junk_clean_button),
            garbageGroups = getEmptyGarbageGroups(
                isInProgress = true,
                alpha = 1.0f
            ),
            isShowPermissionRequired = false,
            buttonOpacity = 0.5f,
            message = context.getString(R.string.junk_clean_recomendation),
            messageColor = presentProgressMessageColor(isCleaned),
            sizeMessageColor = presentProgressMessageColor(isCleaned),
            isInfoVisible = true
        )

    }



    private fun presentProgressMessageColor(isCleaned: Boolean) : Int{
        val id = if (isCleaned){
            general.R.color.secondary
        } else {
            general.R.color.error
        }
        return context.getColor(id)
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



    private fun presentProgressSize() : String{
        return formatFileSize(context, 10_000_000).replace("10,00", "...")
            .replace("10.00", "...")
    }


}