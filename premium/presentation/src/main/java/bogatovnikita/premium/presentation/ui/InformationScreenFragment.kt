package bogatovnikita.premium.presentation.ui

import android.app.Dialog
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Shader
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import bogatovnikita.premium.presentation.R
import bogatovnikita.premium.presentation.WebViewActivity
import bogatovnikita.premium.presentation.databinding.FragmentInformationScreenBinding
import by.kirich1409.viewbindingdelegate.viewBinding
import com.smart.cleaner.phoneclean.settings.Settings
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class InformationScreenFragment : DialogFragment(R.layout.fragment_information_screen) {

    @Inject
    lateinit var settings: Settings

    private val binding: FragmentInformationScreenBinding by viewBinding()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        settings.saveOpenInformation(true)
        setStyle(STYLE_NO_FRAME, general.R.style.Dialog)
        return super.onCreateDialog(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setVersionCode()
        setGradientColor()
        initClickListener()
        binding.cancelSubscription.isVisible = true //TODO удалить при добавлении рекламы
//        binding.cancelSubscription.isVisible = !requireContext().hasSubscription()//TODO раскоментить
    }

    private fun setVersionCode() {
        var version = ""
        try {
            version =
                requireActivity().packageManager.getPackageInfo(
                    requireActivity().packageName,
                    0
                ).versionName
        } catch (e: java.lang.Exception) {
        }
        binding.versionCode.text = getString(R.string.version_code_S, version)
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
        binding.cancelSubscription.setOnClickListener {
            startActivity(
                WebViewActivity.getIntent(
                    requireContext(),
                    WebViewActivity.HOW_TO_CANCEL_SUBSCRIPTION
                )
            )
        }

        binding.termsAndConditions.setOnClickListener {
            startActivity(
                WebViewActivity.getIntent(
                    requireContext(),
                    WebViewActivity.TERMS_AND_CONDITIONS
                )
            )

        }

        binding.privacyPolicy.setOnClickListener {
            startActivity(
                WebViewActivity.getIntent(
                    requireContext(),
                    WebViewActivity.PRIVACY_POLICY
                )
            )
        }
    }

    override fun onDestroy() {
        settings.saveOpenInformation(false)
        super.onDestroy()
    }
}