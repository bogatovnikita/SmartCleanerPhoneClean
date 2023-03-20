package com.bogatovnikita.language_dialog.ui

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bogatovnikita.language_dialog.R
import com.bogatovnikita.language_dialog.adapter.LocalAdapter
import com.bogatovnikita.language_dialog.utils.LocaleProvider

class LocalDialog(context: Context, private val onLocalChange: () -> Unit) : AlertDialog(context) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_local)
        initLocales()
    }

    private fun initLocales() {
        val adapter = LocalAdapter {
            LocaleProvider(context).saveNewLocale(it)
            onLocalChange.invoke()
            cancel()
        }
        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView?.layoutManager = GridLayoutManager(context, 3)
        recyclerView?.adapter = adapter
    }
}