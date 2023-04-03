package yin_kio.garbage_clean.presentation

import android.content.Context
import android.text.format.Formatter.formatFileSize

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

}