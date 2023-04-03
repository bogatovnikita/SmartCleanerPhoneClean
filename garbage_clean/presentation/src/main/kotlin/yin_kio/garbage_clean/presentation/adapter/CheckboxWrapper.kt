package yin_kio.garbage_clean.presentation.adapter


import android.widget.CheckBox
import yin_kio.garbage_clean.domain.ui_out.Checkable

class CheckboxWrapper(
    private val checkbox: CheckBox
) : Checkable {

    override fun setChecked(isChecked: Boolean) {
        checkbox.isChecked = isChecked
    }

}