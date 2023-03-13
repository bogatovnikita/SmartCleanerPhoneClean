package yin_kio.garbage_clean.data

import android.content.Context
import yin_kio.garbage_clean.domain.gateways.NoDeletableFiles

class NoDeletableFilesImpl(
    context: Context
) : NoDeletableFiles {

    private val prefs = context.getSharedPreferences(NO_DELETABLE_FILES, Context.MODE_PRIVATE)

    override fun get(): Set<String> {
        return prefs.getStringSet(NO_DELETABLE_FILES, setOf())!!
    }

    override fun save(paths: List<String>) {
        prefs.edit().putStringSet(NO_DELETABLE_FILES, paths.toSet()).apply()
    }

    companion object{
        private const val NO_DELETABLE_FILES = "NO_DELETABLE_FILES"
    }
}