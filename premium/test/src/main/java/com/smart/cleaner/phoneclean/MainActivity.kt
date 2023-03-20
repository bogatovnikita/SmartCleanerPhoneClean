package com.smart.cleaner.phoneclean

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import bogatovnikita.premium.presentation.ui.PremiumScreenFragment
import com.example.ads.initSubscription

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initSubscription()
        this.supportFragmentManager.beginTransaction()
            .add(R.id.fragment_container, PremiumScreenFragment()).commit()
    }
}