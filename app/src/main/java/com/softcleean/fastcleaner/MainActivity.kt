package com.softcleean.fastcleaner

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.softcleean.fastcleaner.data.battery_provider.BatteryChargeReceiver
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var batteryChargeReceiver: BatteryChargeReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        initAds()
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