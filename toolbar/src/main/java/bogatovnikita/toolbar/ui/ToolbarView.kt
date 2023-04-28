package bogatovnikita.toolbar.ui

import Const.DEEP_LINK_TO_INFORMATION_DIALOG
import Const.DEEP_LINK_TO_LANGUAGE_DIALOG
import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.net.Uri
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat.startActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import bogatovnikita.toolbar.R
import bogatovnikita.toolbar.databinding.ViewToolbarBinding
import com.bogatovnikita.language_dialog.utils.LocaleProvider
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ToolbarView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr, defStyleRes) {

    @Inject
    lateinit var localeProvider: LocaleProvider

    val binding: ViewToolbarBinding

    init {
        val inflater = LayoutInflater.from(context)
        inflater.inflate(R.layout.view_toolbar, this, true)
        binding = ViewToolbarBinding.bind(this)
        initializeAttributes(attrs, defStyleAttr, defStyleRes)
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

        val isLoadLanguage = typedArray.getBoolean(R.styleable.ToolbarView_is_load_language, true)
        if (isLoadLanguage) renderLanguageFlag()

        typedArray.recycle()
    }

    private fun renderLanguageFlag() {
        binding.btnChangeLanguage.setImageResource(localeProvider.getCurrentLocaleModel().image)
    }

    private fun initClickListeners() {
        binding.btnChangeLanguage.setOnClickListener {
            openLanguageDialog()
        }

        binding.btnMenu.setOnClickListener {
            showMenu(it)
        }

        binding.btnCrossExit.setOnClickListener {
            findNavController()?.popBackStack()
        }
    }

    private fun findNavController(): NavController? {
        val activity = getActivity() as AppCompatActivity
        activity.supportFragmentManager.fragments.forEach {
            if (it is NavHostFragment) {
                return it.findNavController()
            }
        }
        return null
    }

    private fun View.getActivity(): Activity? {
        var context = context
        while (context is ContextWrapper) {
            if (context is Activity) {
                return context
            }
            context = context.baseContext
        }
        return null
    }


    private fun openLanguageDialog() {
        val uri = Uri.parse(DEEP_LINK_TO_LANGUAGE_DIALOG)
        findNavController()?.navigate(uri)
    }

    private fun showMenu(it: View) {
        val popupMenu = PopupMenu(context, it)
//        val menu = if (context.hasSubscription()) R.menu.menu_no_ads else R.menu.menu //TODO реклама
        val menu = R.menu.menu_no_ads // убрать при добавлении рекламы
        popupMenu.inflate(menu)
        popupMenu.setOnMenuItemClickListener(onMenuItemClickListener())
        popupMenu.setOnDismissListener { }
        popupMenu.setForceShowIcon(true)
        popupMenu.show()
    }

    private fun onMenuItemClickListener() =
        object : PopupMenu.OnMenuItemClickListener {
            override fun onMenuItemClick(item: MenuItem?): Boolean {
                when (item?.itemId) {
                    R.id.share_menu -> {
                        shareApp()
                        return true
                    }
//                    R.id.ads_off_menu -> { //TODO реклама
//                        val uri = Uri.parse(DEEP_LINK_TO_PREMIUM_DIALOG)
//                        findNavController()?.navigate(uri)
//                        return true
//                    }
                    R.id.information_menu -> {
                        val uri = Uri.parse(DEEP_LINK_TO_INFORMATION_DIALOG)
                        findNavController()?.navigate(uri)
                        return true
                    }
                }
                return false
            }

        }

    private fun shareApp() {
        val shareIntent = Intent()
        shareIntent.action = Intent.ACTION_SEND
        shareIntent.type = "text/plain"
        shareIntent.putExtra(
            Intent.EXTRA_TEXT,
            "http://play.google.com/store/apps/details?id=${context.packageName}"
        );
        startActivity(
            context,
            Intent.createChooser(
                shareIntent,
                context.getString(R.string.look_at_what_a_cool_app)
            ),
            null
        )
    }

}
