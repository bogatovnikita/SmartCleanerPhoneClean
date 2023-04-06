package com.smart.cleaner.phoneclean

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.NavHostFragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bogatovnikita.language_dialog.language.Language
import com.example.ads.initAds
import com.example.ads.initSubscription
import com.smart.cleaner.phoneclean.databinding.ActivityMainBinding
import com.smart.cleaner.phoneclean.settings.Settings
import com.smart.cleaner.phoneclean.ui.dialogs.ShowStartLanguageDialog
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity(R.layout.activity_main), View.OnClickListener {

    @Inject
    lateinit var settings: Settings

    private lateinit var language: Language

    private val binding: ActivityMainBinding by viewBinding()

    override fun attachBaseContext(newBase: Context) {
        language = Language(newBase)
        super.attachBaseContext(language.changeLanguage())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initSubscription()
        initAds()
        initListeners()
        initChangeDestinationListener()
    }

    private fun initListeners() {
        binding.btnBoost.setOnClickListener(this)
        binding.btnClean.setOnClickListener(this)
        binding.btnDuplicate.setOnClickListener(this)
        binding.btnBattery.setOnClickListener(this)
        binding.btnPaywall.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        renderNavBar(view.id)
        when (view.id) {
            R.id.btn_boost -> navigate(R.id.action_to_boostFragment)
            R.id.btn_clean -> {}
            R.id.btn_duplicate -> navigate(R.id.action_to_duplicates_graph)
            R.id.btn_battery -> navigate(R.id.action_to_batteryFragment)
            R.id.btn_paywall -> navigate(R.id.action_to_premiumScreenFragment)
        }
    }

    private fun navigate(navigateId: Int) {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        navController.navigate(navigateId)
    }

    private fun initChangeDestinationListener() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.boostFragment -> {
                    renderNavBar(binding.btnBoost.id, binding.titleBoost.id)
                    checkShowFirstLanguageDialog()
                }
                R.id.batteryFragment -> renderNavBar(binding.btnBattery.id, binding.titleBattery.id)
                R.id.premiumScreenFragment -> renderNavBar(binding.btnPaywall.id)
                com.smart.cleaner.phoneclean.presentation.R.id.duplicateImagesFragment,
                com.smart.cleaner.phoneclean.presentation.R.id.duplicateFilesFragment -> renderNavBar(
                    binding.btnDuplicate.id,
                    binding.titleDuplicate.id
                )
            }
        }
    }

    private fun checkShowFirstLanguageDialog() {
        if (settings.getFirstLaunch() && language.checkLanguage()) {
            ShowStartLanguageDialog().show(supportFragmentManager, "")
            settings.saveFirstLaunch()
        }
    }

    private fun renderNavBar(currentDestination: Int, currentTitleDestination: Int? = null) {
        val listButton =
            listOf(
                binding.btnBoost,
                binding.btnClean,
                binding.btnDuplicate,
                binding.btnBattery,
                binding.btnPaywall
            )
        listButton.forEach { image ->
            image.setImageDrawable(
                AppCompatResources.getDrawable(
                    this,
                    iconId(image.id, currentDestination)
                )
            )
        }
        if (currentTitleDestination != null) renderTextColor(currentTitleDestination)
    }

    private fun renderTextColor(currentTitleDestination: Int) {
        val titleList = listOf(
            binding.titleBoost,
            binding.titleClean,
            binding.titleDuplicate,
            binding.titleBattery,
            binding.titlePaywall
        )
        titleList.forEach {
            if (currentTitleDestination == it.id) {
                it.setTextColor(ContextCompat.getColor(this, general.R.color.primary))
            } else {
                it.setTextColor(ContextCompat.getColor(this, general.R.color.grey_light))
            }
        }
    }

    private fun iconId(destination: Int, currentDestination: Int): Int {
        val isActive = destination == currentDestination
        return if (isActive) {
            when (destination) {
                R.id.btn_boost -> general.R.drawable.ic_boost_danger
                R.id.btn_clean -> general.R.drawable.ic_clean_danger
                R.id.btn_duplicate -> general.R.drawable.ic_duplicate_danger
                R.id.btn_battery -> general.R.drawable.ic_battery_danger
                R.id.btn_paywall -> R.drawable.ic_paywall_off
                else -> general.R.drawable.ic_clean_danger
            }
        } else {

            when (destination) {
                R.id.btn_boost -> R.drawable.ic_grey_light_bulb
                R.id.btn_clean -> R.drawable.ic_grey_trash
                R.id.btn_duplicate -> R.drawable.ic_grey_duplicate
                R.id.btn_battery -> R.drawable.ic_grey_battery
                R.id.btn_paywall -> R.drawable.ic_paywall_off
                else -> R.drawable.ic_grey_light_bulb
            }
        }
    }
}