package com.softcleean.fastcleaner.custom

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import com.softcleean.fastcleaner.databinding.ChoosingTypeBatteryBarViewBinding


class ChoosingTypeBatteryBar @JvmOverloads constructor(
    context: Context,
    private val attr: AttributeSet? = null,
    private val defStyleAttr: Int = 0
) : ConstraintLayout(context, attr, defStyleAttr) {

    private val _binding: ChoosingTypeBatteryBarViewBinding =
        ChoosingTypeBatteryBarViewBinding.inflate(LayoutInflater.from(context), this)

    fun setOnChangeTypeListener(block: (type: String) -> Unit) {
        with(_binding) {
            btnNormalOff.setOnClickListener {
                block(NORMAL)
                renderSaveTypeBattery(btnNormalOn)
            }
            btnUltraOff.setOnClickListener {
                block(ULTRA)
                renderSaveTypeBattery(btnUltraOn)
            }
            btnExtraOff.setOnClickListener {
                block(EXTRA)
                renderSaveTypeBattery(btnExtraOn)
            }
        }
    }

    private fun renderSaveTypeBattery(type: ImageView) {
        with(_binding) {
            val listId = listOf(btnNormalOn, btnUltraOn, btnExtraOn)
            listId.forEach {
                it.isVisible = type == it
            }
        }
        setAlpha()
    }

    private fun setAlpha() {
        _binding.tvTypeNormal.alpha = if (_binding.btnNormalOn.isVisible) 1F else 0.5F
        _binding.tvTypeExtra.alpha = if (_binding.btnExtraOn.isVisible) 1F else 0.5F
        _binding.tvTypeUltra.alpha = if (_binding.btnUltraOn.isVisible) 1F else 0.5F
    }


    fun setSaveTypeBattery(type: String) {
        with(_binding) {
            when (type) {
                NORMAL -> {
                    renderSaveTypeBattery(btnNormalOn)
                }
                ULTRA -> {
                    renderSaveTypeBattery(btnUltraOn)
                }
                EXTRA -> {
                    renderSaveTypeBattery(btnExtraOn)
                }
                else -> {}
            }
        }
        setAlpha()
    }

    companion object {
        const val NORMAL = "NORMAL"
        const val ULTRA = "ULTRA"
        const val EXTRA = "EXTRA"
    }

}