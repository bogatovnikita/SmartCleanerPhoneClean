package com.entertainment.event.ssearch.ar155

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.entertainment.event.ssearch.data.battery_provider.BatteryChargeReceiver
import dagger.hilt.android.AndroidEntryPoint
import initAds
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var batteryChargeReceiver: BatteryChargeReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.AppTheme)
        setContentView(R.layout.activity_main)
        initAds()
    }

    override fun onResume() {
        super.onResume()
        batteryChargeReceiver.registerReceiver(this)
    }

    override fun onPause() {
        super.onPause()
        batteryChargeReceiver.unregisterReceiver(this)
    }
}