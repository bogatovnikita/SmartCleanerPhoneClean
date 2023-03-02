package com.softcleean.fastcleaner

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.content.res.AppCompatResources
import androidx.navigation.fragment.NavHostFragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.softcleean.fastcleaner.ads.AdsViewModel
import com.softcleean.fastcleaner.data.battery_provider.BatteryChargeReceiver
import com.softcleean.fastcleaner.databinding.ActivityMainBinding
import com.softcleean.fastcleaner.utils.showNotification
import dagger.hilt.android.AndroidEntryPoint
import initAdsAndAppOpen
import initPostbackProvider
import showInterstitial
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity(R.layout.activity_main), View.OnClickListener {

    @Inject
    lateinit var batteryChargeReceiver: BatteryChargeReceiver
    private val viewModel: AdsViewModel by viewModels()

    private val binding: ActivityMainBinding by viewBinding()

    override fun onCreate(savedInstanceState: Bundle?) {
        initAdsAndAppOpen()
        initPostbackProvider()
        super.onCreate(savedInstanceState)
        initListeners()
        initChangeDestinationListener()
    }

    private fun initListeners() {
        binding.btnBoost.setOnClickListener(this)
        binding.btnCool.setOnClickListener(this)
        binding.btnBattery.setOnClickListener(this)
        binding.btnClean.setOnClickListener(this)
    }

    override fun onResume() {
        super.onResume()
        batteryChargeReceiver.registerReceiver(this)
    }

    override fun onPause() {
        super.onPause()
        batteryChargeReceiver.unregisterReceiver(this)
    }

    override fun onClick(view: View) {
        renderNavBar((view.id))
        when (view.id) {
            R.id.btn_boost -> navigateAndShowInter(R.id.action_to_boostFragment)
            R.id.btn_cool -> navigateAndShowInter(R.id.action_to_coolingFragment)
            R.id.btn_battery -> navigateAndShowInter(R.id.action_to_batteryFragment)
            R.id.btn_clean -> navigateAndShowInter(R.id.action_to_cleanFragment)
        }
    }

    private fun navigateAndShowInter(navigateId: Int) {
        if (viewModel.isCanShowInter()) {
            viewModel.cantShowInter()
            showInterstitial()
        }
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        navController.navigate(navigateId)
    }

    private fun initChangeDestinationListener() {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.boostFragment -> renderNavBar(binding.btnBoost.id)
                R.id.cleanFragment -> renderNavBar(binding.btnClean.id)
                R.id.coolingFragment-> renderNavBar(binding.btnCool.id)
                R.id.batteryFragment -> renderNavBar(binding.btnBattery.id)
            }
        }
    }

    private fun renderNavBar(currentDestination: Int) {
        val listButton =
            listOf(binding.btnBoost, binding.btnCool, binding.btnBattery, binding.btnClean)
        listButton.forEach { image ->
            image.setImageDrawable(
                AppCompatResources.getDrawable(
                    this,
                    iconId(image.id, currentDestination)
                )
            )
        }
    }

    private fun iconId(destination: Int, currentDestination: Int): Int {
        val isActive = destination == currentDestination
        return if (isActive) {
            when (destination) {
                R.id.btn_boost -> R.drawable.ic_boost_danger
                R.id.btn_cool -> R.drawable.ic_cooling_danger
                R.id.btn_battery -> R.drawable.ic_battery_danger
                R.id.btn_clean -> R.drawable.ic_clean_danger
                else -> R.drawable.ic_clean_danger
            }
        } else {

            when (destination) {
                R.id.btn_boost -> R.drawable.ic_grey_rocket
                R.id.btn_cool -> R.drawable.ic_grey_fire
                R.id.btn_battery -> R.drawable.ic_grey_battery
                R.id.btn_clean -> R.drawable.ic_grey_bucket
                else -> R.drawable.ic_grey_bucket
            }

        }
    }
}