package bogatovnikita.premium.presentation.ui

import android.app.Dialog
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Shader
import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import bogatovnikita.premium.presentation.R
import bogatovnikita.premium.presentation.databinding.FragmentInformationScreenBinding
import by.kirich1409.viewbindingdelegate.viewBinding

class InformationScreenFragment : DialogFragment(R.layout.fragment_information_screen) {

    private val binding: FragmentInformationScreenBinding by viewBinding()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        setStyle(STYLE_NO_FRAME, general.R.style.Dialog)
        return super.onCreateDialog(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setGradientColor()
        initClickListener()
    }

    private fun setGradientColor() {
        val shader = LinearGradient(
            0f, 0f, 0f, binding.titleName.textSize,
            intArrayOf(Color.parseColor("#7a8ad5"), Color.parseColor("#BAC4F1")),
            null, Shader.TileMode.CLAMP
        )
        binding.titleName.paint.shader = shader
    }

    private fun initClickListener() {

    }
}