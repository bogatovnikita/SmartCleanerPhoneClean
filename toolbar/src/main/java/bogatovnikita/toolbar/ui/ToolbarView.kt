package bogatovnikita.toolbar.ui

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import bogatovnikita.toolbar.R
import bogatovnikita.toolbar.databinding.ViewToolbarBinding
import com.bogatovnikita.language_dialog.utils.LocaleProvider
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import javax.inject.Singleton

typealias OnOpenLocaleDialog = () -> Unit

@Singleton
@AndroidEntryPoint
class ToolbarView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr, defStyleRes) {

    @Inject
    lateinit var localeProvider: LocaleProvider

    private val binding: ViewToolbarBinding
    private lateinit var listener: OnOpenLocaleDialog

    init {
        val inflater = LayoutInflater.from(context)
        inflater.inflate(R.layout.view_toolbar, this, true)
        binding = ViewToolbarBinding.bind(this)
        initializeAttributes(attrs, defStyleAttr, defStyleRes)
        renderState()
        initClickListeners()
    }

    private fun initializeAttributes(attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) {
        if (attrs == null) return
        val typedArray = context.obtainStyledAttributes(
            attrs,
            R.styleable.ToolbarView,
            defStyleAttr,
            defStyleRes
        )
        val titleTextToolbar = typedArray.getString(R.styleable.ToolbarView_title_text)
        binding.title.text = titleTextToolbar ?: ""

        val showFlagToolbar = typedArray.getBoolean(R.styleable.ToolbarView_show_flag, true)
        binding.btnChangeLanguage.visibility = if (showFlagToolbar) VISIBLE else GONE

        val showCrossToolbar = typedArray.getBoolean(R.styleable.ToolbarView_show_cross, false)
        binding.btnCrossExit.visibility = if (showCrossToolbar) VISIBLE else GONE

        val showMenuToolbar = typedArray.getBoolean(R.styleable.ToolbarView_show_menu, true)
        binding.btnMenu.visibility = if (showMenuToolbar) VISIBLE else GONE

        typedArray.recycle()
    }

    fun setListener(onOpenLocaleDialog: OnOpenLocaleDialog) {
        this.listener = onOpenLocaleDialog
    }

    private fun renderState() {
        binding.btnChangeLanguage.setImageResource(localeProvider.getCurrentLocaleModel().image)
    }

    private fun initClickListeners() {
        binding.btnChangeLanguage.setOnClickListener {
            listener.invoke()
        }
    }

}