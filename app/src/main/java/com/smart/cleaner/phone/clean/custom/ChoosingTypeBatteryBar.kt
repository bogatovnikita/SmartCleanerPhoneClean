package com.smart.cleaner.phone.clean.custom

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import com.smart.cleaner.phone.clean.R
import com.smart.cleaner.phone.clean.databinding.ChoosingTypeBatteryBarViewBinding


class ChoosingTypeBatteryBar @JvmOverloads constructor(
    context: Context,
    attr: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attr, defStyleAttr) {

    private val _binding: ChoosingTypeBatteryBarViewBinding =
        ChoosingTypeBatteryBarViewBinding.inflate(LayoutInflater.from(context), this)

    fun setOnChangeTypeListener(block: (type: String) -> Unit) {
        with(_binding) {
            containerNormal.setOnClickListener {
                block(NORMAL)
                renderSaveTypeBattery(btnNormalOn, containerNormal)
            }
            containerUltra.setOnClickListener {
                block(ULTRA)
                renderSaveTypeBattery(btnUltraOn, containerUltra)
            }
            containerExtra.setOnClickListener {
                block(EXTRA)
                renderSaveTypeBattery(btnExtraOn, containerExtra)
            }
        }
    }

    private fun renderSaveTypeBattery(radioButton: ImageView, container: View) {
        with(_binding) {
            val listBtnId = listOf(btnNormalOn, btnUltraOn, btnExtraOn)
            val listContainerId = listOf(containerNormal, containerUltra, containerExtra)
            listBtnId.forEach {
                it.visibility = if (radioButton == it) VISIBLE else INVISIBLE
            }
            listContainerId.forEach {
                val id =
                    if (container == it) R.drawable.bg_battery_container_checked else R.drawable.bg_battery_container_unchecked
                it.setBackgroundResource(id)
            }
        }
    }


    fun setTypeBattery(type: String) {
        with(_binding) {
            when (type) {
                NORMAL -> {
                    renderSaveTypeBattery(btnNormalOn, containerNormal)
                }
                ULTRA -> {
                    renderSaveTypeBattery(btnUltraOn, containerUltra)
                }
                EXTRA -> {
                    renderSaveTypeBattery(btnExtraOn, containerExtra)
                }
                else -> {}
            }
        }
    }

    companion object {
        const val NORMAL = "NORMAL"
        const val ULTRA = "ULTRA"
        const val EXTRA = "EXTRA"
    }

}