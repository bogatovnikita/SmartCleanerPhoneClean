package com.smart.cleaner.phone.clean

import android.app.NotificationManager
import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO
import androidx.navigation.fragment.NavHostFragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bogatovnikita.language_dialog.language.Language
import com.smart.cleaner.phone.clean.databinding.ActivityMainBinding
import com.smart.cleaner.phone.clean.receiver.AlarmManagerReceiver
import com.smart.cleaner.phoneclean.settings.Settings
import com.smart.cleaner.phone.clean.ui.dialogs.ShowStartLanguageDialog
import com.smart.cleaner.phoneclean.ui_core.adapters.GetIdForNavigation
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity(R.layout.activity_main), GetIdForNavigation {

    @Inject
    lateinit var settings: Settings

    private lateinit var language: Language

    private val binding: ActivityMainBinding by viewBinding()

    override fun attachBaseContext(newBase: Context) {
        language = Language(newBase)
        super.attachBaseContext(language.changeLanguage())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_NO)
        super.onCreate(savedInstanceState)
        registerAlarmManagerAndCancelNotification()
//        initAdsAndSubscription() //TODO реклама
        initMenu()
        initClickListenerMenu()
//        testNav()
    }

    private fun registerAlarmManagerAndCancelNotification() {
        val notificationManager =
            this.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancel(123)
        AlarmManagerReceiver(this).apply {
            cancelAlarmManager()
            startAlarmManager()
        }
    }

//    private fun initAdsAndSubscription() {
//        initAds()
//        emulateSubscription()//TODO убрать когда выйдет из беты
//        if (hasSubscription()) {
//            emulateSubscription()
//        } else {
//            initSubscription()
//        }
//    }

    private fun initMenu() {
        //TODO сделать проверку на рекламу и установить нужное меню
        binding.bottomNavigationView.inflateMenu(R.menu.menu_without_ads)
        binding.bottomNavigationView.itemIconTintList = null
    }

    private fun initClickListenerMenu() {
        binding.bottomNavigationView.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.btn_boost -> {
                    navigate(R.id.action_to_boostFragment)
                    checkShowFirstLanguageDialog()
                    true
                }
                R.id.btn_clean -> {
                    navigate(R.id.action_to_garbage_clean_graph)
                    true
                }
                R.id.btn_duplicate -> {
                    navigate(R.id.action_to_duplicates_graph)
                    true
                }
                R.id.btn_battery -> {
                    navigate(R.id.action_to_batteryFragment)
                    true
                }
//                R.id.btn_paywall -> {
//                    navigate(R.id.action_to_premiumScreenFragment)
//                    true
//                } // TODO реклама
                else -> false
            }
        }
    }

    private fun navigate(navigateId: Int) {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        navController.navigate(navigateId)
    }

    private fun checkShowFirstLanguageDialog() {
        if (settings.getFirstLaunch() && language.checkLanguage()) {
            ShowStartLanguageDialog().show(supportFragmentManager, "")
            settings.saveFirstLaunch()
        }
    }

    override fun openBoostMenu() {
        binding.bottomNavigationView.selectedItemId = R.id.btn_boost
    }

    override fun openCleanMenu() {
        binding.bottomNavigationView.selectedItemId = R.id.btn_clean
    }

    override fun openDuplicatesMenu() {
        binding.bottomNavigationView.selectedItemId = R.id.btn_duplicate
    }

    override fun openBatteryMenu() {
        binding.bottomNavigationView.selectedItemId = R.id.btn_battery
    }

}