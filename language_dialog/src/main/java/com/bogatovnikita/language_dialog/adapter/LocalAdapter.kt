package com.bogatovnikita.language_dialog.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bogatovnikita.language_dialog.R
import com.bogatovnikita.language_dialog.utils.LocaleProvider
import kotlinx.android.synthetic.main.item_locale.view.*

class LocalAdapter(private val onLocalSelected: (LocaleProvider.LocaleModel) -> Unit) :
    RecyclerView.Adapter<LocalAdapter.ViewHolder>() {

    private val locales = LocaleProvider.LocaleModel.getValues()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_locale, parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(locales[position])
    }

    override fun getItemCount() = locales.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun onBind(localeModel: LocaleProvider.LocaleModel) {
            itemView.iv_flag.setImageResource(localeModel.image)
            itemView.setOnClickListener {
                onLocalSelected(localeModel)
            }
        }
    }
}